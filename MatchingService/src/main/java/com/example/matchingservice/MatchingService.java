package com.example.matchingservice;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

public class MatchingService {

    private final ObservableList<Capsule> capsules;
    private ObservableList<ArrayList<Integer>> randomNumbers;
    private ObservableList<ArrayList<String>> hashes;
    private ObservableList<ArrayList<Timestamp>> timestamps;
    private ObservableList<ArrayList<String>> tokens;

    public MatchingService() {
        this.capsules = FXCollections.observableArrayList(new ArrayList<>());
        // Capsules need to be deleted after predefined interval(in this case 7 days)
        Timer timer = new Timer ();
        TimerTask weeklyTask = new TimerTask () {
            @Override
            public void run () {
                removeCapsules();
            }
        };
        timer.schedule (weeklyTask, 0L, 7*24*1000*60*60);

        this.randomNumbers = FXCollections.observableList(new ArrayList<>());
        this.hashes = FXCollections.observableList(new ArrayList<>());
        this.timestamps = FXCollections.observableList(new ArrayList<>());
        this.tokens = FXCollections.observableList(new ArrayList<>());
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
        this.randomNumbers.add(randomNumbers);
        this.hashes.add(hashes);
        this.timestamps.add(timestamps);
        this.tokens.add(tokens);
    }
}
