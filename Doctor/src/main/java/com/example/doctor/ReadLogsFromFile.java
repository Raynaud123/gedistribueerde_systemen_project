package com.example.doctor;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class ReadLogsFromFile {

    private String phoneNumber;
    private ArrayList<Integer> randomNumbers;
    private ArrayList<String> hashes;
    private ArrayList<Timestamp> timestamps;
    private ArrayList<String> tokens;

    public ReadLogsFromFile(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        randomNumbers = new ArrayList<>();
        hashes = new ArrayList<>();
        timestamps = new ArrayList<>();
        tokens = new ArrayList<>();
    }

    public void readJSONFile() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("Doctor\\src\\main\\JSON\\logs-" + phoneNumber + ".json"));
            JSONObject jsonObject = (JSONObject) obj;
            phoneNumber = (String) jsonObject.get("phoneNumber");
            randomNumbers = (ArrayList<Integer>) jsonObject.get("randomNumbers");
            hashes = (ArrayList<String>) jsonObject.get("hashes");
            timestamps = (ArrayList<Timestamp>) jsonObject.get("timestamps");
            tokens = (ArrayList<String>) jsonObject.get("tokens");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> getRandomNumbers() {
        return randomNumbers;
    }

    public ArrayList<String> getHashes() {
        return hashes;
    }

    public ArrayList<Timestamp> getTimestamps() {
        return timestamps;
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }

}
