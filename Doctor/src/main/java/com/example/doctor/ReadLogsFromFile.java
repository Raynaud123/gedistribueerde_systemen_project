package com.example.doctor;

import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class ReadLogsFromFile {

    private String phoneNumber;
    private final ArrayList<Integer> randomNumbers;
    private final ArrayList<String> hashes;
    private final ArrayList<Timestamp> timestamps;
    private final ArrayList<String> tokens;

    public ReadLogsFromFile(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        randomNumbers = new ArrayList<>();
        hashes = new ArrayList<>();
        timestamps = new ArrayList<>();
        tokens = new ArrayList<>();
    }

    public void readJSONFile() throws ParseException {
        JSONParser parser = new JSONParser();
        JSONArray randomNumberJSONArray = new JSONArray();
        JSONArray hashesJSONArray = new JSONArray();
        JSONArray timestampsJSONArray = new JSONArray();
        JSONArray tokensJSONArray = new JSONArray();

        try {
            FileReader fileReader = new FileReader("Doctor\\src\\main\\JSON\\logs-" + phoneNumber + ".json");
            Object obj = parser.parse(fileReader);
            JSONObject jsonObject = (JSONObject) obj;
            phoneNumber = (String) jsonObject.get("phoneNumber");
            randomNumberJSONArray = (JSONArray) jsonObject.get("randomNumbers");
            hashesJSONArray = (JSONArray) jsonObject.get("hashes");
            timestampsJSONArray = (JSONArray) jsonObject.get("timestamps");
            tokensJSONArray = (JSONArray) jsonObject.get("tokens");

            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Object o : randomNumberJSONArray) randomNumbers.add(Integer.parseInt(o.toString()));
        for (Object o : hashesJSONArray) hashes.add(o.toString());
        for (Object o : timestampsJSONArray) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse((String) o);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            timestamps.add(timestamp);
        }
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
