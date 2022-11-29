package com.example.visitor;

import java.sql.Timestamp;

public class Capsule {
        private String hash;
        private Timestamp ts;


    public Capsule(String hash, Timestamp ts) {
        this.hash = hash;
        this.ts = ts;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }
}
