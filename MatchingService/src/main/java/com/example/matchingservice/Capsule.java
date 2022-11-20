package com.example.matchingservice;

import java.sql.Timestamp;

public class Capsule {
    private Timestamp timeInterval;
    private String userToken;
    private String hash;


    public Capsule(Timestamp timeInterval, String userToken, String hash) {
        this.timeInterval = timeInterval;
        this.userToken = userToken;
        this.hash = hash;
    }

    public Timestamp getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Timestamp timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}
