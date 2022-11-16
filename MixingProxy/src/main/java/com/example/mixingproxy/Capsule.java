package com.example.mixingproxy;

import java.sql.Timestamp;


public class Capsule {
    private Timestamp timeInterval;
    private String userToken;
    private String hex;


    public Capsule(Timestamp timeInterval, String userToken, String hex) {
        this.timeInterval = timeInterval;
        this.userToken = userToken;
        this.hex = hex;
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

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }
}
