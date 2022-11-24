package com.example.matchingservice;

import com.example.registrar.RegistrarInterface;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MatchingService {

    private final ObservableList<Capsule> capsules;
    public Registry registrarRegistry;
    public RegistrarInterface registrarInterface;


    public MatchingService()  {
        this.capsules = FXCollections.observableArrayList(new ArrayList<>());


        try {
            registrarRegistry = LocateRegistry.getRegistry("localhost", 1112);
            registrarInterface = (RegistrarInterface) registrarRegistry.lookup("RegistrarService");
        }catch (Exception e){
            System.out.println("error connecting to registrar");
        }

        // Capsules need to be deleted after predefined interval(in this case 7 days)
        Timer timer = new Timer ();
        TimerTask weeklyTask = new TimerTask () {
            @Override
            public void run () {
                removeCapsules();
            }
        };
        TimerTask dailyTask =  new TimerTask () {
            @Override
            public void run () {
                try {
                    sentUninformedTokens();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule (weeklyTask, 0L, 7*24*1000*60*60);
        timer.schedule(dailyTask,0L, 24*1000*60*60);


    }


    public void addCapsule(String hash, Timestamp timeInterval, String userToken) {
        capsules.add(new Capsule(timeInterval, userToken, hash));
    }

    public ObservableList<Capsule> getCapsules() {
        return capsules;
    }

    public void removeCapsules(){
        capsules.clear();
    }

    public void saveInfectedLogs(ArrayList<Integer> randomNumbers, ArrayList<String> hashes, ArrayList<Timestamp> timestamps, ArrayList<String> tokens) {
        for(int i = 0 ; i < randomNumbers.size(); i++){
            Capsule temp = new Capsule(timestamps.get(i), tokens.get(i),hashes.get(i));
            for (Capsule capsule : capsules) {
                if (temp.equals(capsule)) {
                    capsule.setCritical(true);
                }
            }
        }

//        this.randomNumbers.add(randomNumbers);
//        this.hashes.add(hashes);
//        this.timestamps.add(timestamps);
//        addTokens(tokens);
    }

//    private void addTokens(ArrayList<String> tokens) {
//        ArrayList<Token> temp = new ArrayList<>();
//        for (String token : tokens) {
//            Token t = new Token(token, "uninformed", LocalDateTime.now());
//            temp.add(t);
//        }
//
//        this.tokens.add(temp);
//    }


    public ArrayList<String> fetchCriticalHashes() {
        ArrayList<String> temp = new ArrayList<>();
        for (Capsule capsule : capsules) {
            if (capsule.getCritical()) {
                temp.add(capsule.getHash());
            }
        }
        return temp;
    }

    public  ArrayList<Timestamp> fetchCriticalTimeInterval() {
        ArrayList<Timestamp> temp = new ArrayList<>();
        for (Capsule capsule : capsules) {
            if (capsule.getCritical()) {
                temp.add(capsule.getTimeInterval());
            }
        }
        return temp;
    }

    public void setTokenToInformed(String token) {
        for (Capsule capsule : capsules) {
            if (capsule.getUserToken().equals(token)) {
                capsule.setInformed(true);
                break;
            }
        }
    }

    public void sentUninformedTokens() throws RemoteException {
        ArrayList<String> temp = new ArrayList<>();
        for (Capsule capsule : capsules) {
            if (!capsule.getInformed()) {
                temp.add(capsule.getUserToken());
            }
        }

        registrarInterface.sentUninformedTokens(temp);

    }
}
