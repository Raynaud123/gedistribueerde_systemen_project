package com.example.matchingservice;

import com.example.registrar.RegistrarInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MatchingService {

    private final ObservableList<Capsule> capsules;
    private PublicKey publicKey;
    public RegistrarInterface registrarInterface;


    public MatchingService() {
        this.capsules = FXCollections.observableArrayList(new ArrayList<>());

        Registry registrarRegistry;
        try {
            SslClientSocketFactory csf = new SslClientSocketFactory("client", "clientpw");

            registrarRegistry = LocateRegistry.getRegistry("localhost", 3000, csf);
            registrarInterface = (RegistrarInterface) registrarRegistry.lookup("RegistrarService");
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        // Capsules need to be deleted after predefined interval(in this case 7 days)
        Timer timer = new Timer ();
        TimerTask dailyTask =  new TimerTask () {
            @Override
            public void run () {
                try {
                    sentUninformedTokens();
                    removeOldCapsules();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(dailyTask,24*1000*60*60, 24*1000*60*60);

    }


    public void addCapsule(String hash, Timestamp timeInterval, String userToken) {
        capsules.add(new Capsule(timeInterval, userToken, hash));
    }

    public ObservableList<Capsule> getCapsules() {
        return capsules;
    }


    public void removeOldCapsules() {
        List<Capsule> capsulesToRemove = new ArrayList<>();
        for (Capsule capsule : capsules) {
            if (capsule.getTimeInterval().before(Timestamp.valueOf(LocalDateTime.now().minusDays(7)))) {
                capsulesToRemove.add(capsule);
            }
        }
        capsules.removeAll(capsulesToRemove);
    }

    public void saveInfectedLogs(ArrayList<Integer> randomNumbers, byte[] randomNumbersSigned, ArrayList<String> hashes, byte[] hashesSigned, ArrayList<Timestamp> timestamps, byte[] timestampsSigned, ArrayList<String> tokens, byte[] tokensSigned) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        boolean validateOK = validateSignature(randomNumbers.toString(), randomNumbersSigned);
        if (!validateSignature(hashes.toString(), hashesSigned)) validateOK = false;
        else if (!validateSignature(timestamps.toString(), timestampsSigned)) validateOK = false;
        else if (!validateSignature(tokens.toString(), tokensSigned)) validateOK = false;

        if (validateOK) {
            for(int i = 0 ; i < tokens.size(); i++){
                Capsule temp = new Capsule(timestamps.get(i), tokens.get(i),hashes.get(i));
                for (Capsule capsule : capsules) {
                    if (temp.equals(capsule)) {
                        capsule.setCritical(true);
                    }
                }
            }
        }
    }

    private boolean validateSignature(String toValidateToString, byte[] signed) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initVerify(publicKey);
        sign.update(toValidateToString.getBytes());
        return sign.verify(signed);
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

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
