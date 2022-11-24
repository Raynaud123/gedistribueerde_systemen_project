package com.example.matchingservice;

import java.sql.Timestamp;

public class Capsule {
    private Timestamp timeInterval;
    private String userToken;
    private String hash;
    private Boolean critical;
    private Boolean informed;


    public Capsule(Timestamp timeInterval, String userToken, String hash) {
        this.timeInterval = timeInterval;
        this.userToken = userToken;
        this.hash = hash;
        this.critical = false;
        this.informed = true;
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

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Capsule cap = (Capsule) o;

        return cap.getHash().equals(hash) && cap.getTimeInterval().equals(timeInterval) && cap.getUserToken().equals(userToken);

    }

    public Boolean getCritical() {
        return critical;
    }

    public void setCritical(Boolean critical) {
        this.critical = critical;
    }

    public Boolean getInformed() {
        return informed;
    }

    public void setInformed(Boolean informed) {
        this.informed = informed;
    }
}
