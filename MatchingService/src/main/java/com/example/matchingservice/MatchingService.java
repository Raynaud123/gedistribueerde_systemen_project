package com.example.matchingservice;

import java.sql.Timestamp;
import java.util.ArrayList;

public class MatchingService {
    ArrayList<Capsule> capsules;


    public MatchingService() {
        this.capsules = new ArrayList<>();
    }

    public void addCapsule(String hex, Timestamp timeInterval, String userToken) {
        capsules.add(new Capsule(timeInterval,userToken,hex));
    }
}
