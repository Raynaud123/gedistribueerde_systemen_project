package com.example.mixingproxy;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public class Capsule {
            Timestamp timeInterval;
            String userToken;
            String hex;


    public Capsule(Timestamp timeInterval, String userToken, String hex) {
        this.timeInterval = timeInterval;
        this.userToken = userToken;
        this.hex = hex;
    }
}
