package com.example.matchingservice;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class MatchingService {
    private ArrayList<Capsule> capsules;


    public MatchingService() {
        this.capsules = new ArrayList<>();
        // Capsules need to be deleted after predefined interval(in this case 7 days)
        Timer timer = new Timer ();
        TimerTask weeklyTask = new TimerTask () {
            @Override
            public void run () {
                capsules = new ArrayList<>();
            }
        };
        timer.schedule (weeklyTask, 0L, 7*24*1000*60*60);
    }

    public void addCapsule(String hex, Timestamp timeInterval, String userToken) {
        capsules.add(new Capsule(timeInterval,userToken,hex));
    }
}
