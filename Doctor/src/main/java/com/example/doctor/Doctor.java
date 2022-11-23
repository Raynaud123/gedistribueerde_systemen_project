package com.example.doctor;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Doctor {

    private ArrayList<Integer> randomNumbers;
    private ArrayList<String> hashes;
    private ArrayList<Timestamp> timestamps;
    private ArrayList<String> tokens;

    public void getLogsFromUser(String phoneNumber) {
        ReadLogsFromFile logsFromFile = new ReadLogsFromFile(phoneNumber);
        logsFromFile.readJSONFile();
        this.randomNumbers = logsFromFile.getRandomNumbers();
        this.hashes = logsFromFile.getHashes();
        this.timestamps = logsFromFile.getTimestamps();
        this.tokens = logsFromFile.getTokens();

        System.out.println("Logs are loaded");
    }
}
