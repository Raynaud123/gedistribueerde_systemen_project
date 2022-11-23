package com.example.visitor;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import org.json.simple.JSONObject;

public class ReleaseLogsToFile {

    public static void writeJsonFile(String phoneNumber, ArrayList<Integer> randomNumbers, ArrayList<String> hashes, ArrayList<Timestamp> timestamps, ArrayList<String> tokens) {

        Map obj = new LinkedHashMap();

        // adding value of objective function
        obj.put("phoneNumber", phoneNumber);
        obj.put("randomNumbers", randomNumbers);
        obj.put("hashes", hashes);
        obj.put("timestamps", timestamps);
        obj.put("tokens", tokens);

        try {
            FileWriter file = new FileWriter("Doctor\\src\\main\\JSON\\logs-" + phoneNumber + ".json");
            file.write(JSONObject.toJSONString(obj));
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("JSON file created");

    }
}
