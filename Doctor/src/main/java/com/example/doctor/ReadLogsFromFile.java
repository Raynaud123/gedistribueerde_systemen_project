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
        JSONArray randomNumberJSONArray = new JSONArray();
        JSONArray hashesJSONArray = new JSONArray();
        JSONArray timestampsJSONArray = new JSONArray();
        JSONArray tokensJSONArray = new JSONArray();

        try {
            Object obj = parser.parse(new FileReader("Doctor\\src\\main\\JSON\\logs-" + phoneNumber + ".json"));
            JSONObject jsonObject = (JSONObject) obj;
            phoneNumber = (String) jsonObject.get("phoneNumber");
            randomNumberJSONArray = (JSONArray) jsonObject.get("randomNumbers");
            hashesJSONArray = (JSONArray) jsonObject.get("hashes");
            timestampsJSONArray = (JSONArray) jsonObject.get("timestamps");
            tokensJSONArray = (JSONArray) jsonObject.get("tokens");

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Object o : randomNumberJSONArray) randomNumbers.add(Integer.parseInt(o.toString()));
        for (Object o : hashesJSONArray) hashes.add(o.toString());
        for (Object o : timestampsJSONArray) timestamps.add(Timestamp.valueOf(o.toString()));
        for (Object o : tokensJSONArray) tokens.add(o.toString());

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
