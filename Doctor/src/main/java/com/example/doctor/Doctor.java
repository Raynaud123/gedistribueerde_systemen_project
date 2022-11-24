package com.example.doctor;

import com.example.matchingservice.MatchingServiceInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.security.*;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Doctor {

    private ArrayList<Integer> randomNumbers;
    private ArrayList<String> hashes;
    private ArrayList<Timestamp> timestamps;
    private ArrayList<String> tokens;

    MatchingServiceInterface matchingServiceInterface;
    private final KeyPair keyPair;



    public Doctor(MatchingServiceInterface matchingServiceInterface) throws NoSuchAlgorithmException {
        this.matchingServiceInterface = matchingServiceInterface;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        this.keyPair = keyPairGenerator.generateKeyPair();
    }

    public void getLogsFromUser(String phoneNumber) throws RemoteException {
        ReadLogsFromFile logsFromFile = new ReadLogsFromFile(phoneNumber);
        logsFromFile.readJSONFile();
        this.randomNumbers = logsFromFile.getRandomNumbers();
        this.hashes = logsFromFile.getHashes();
        this.timestamps = logsFromFile.getTimestamps();
        this.tokens = logsFromFile.getTokens();

        System.out.println("Logs are loaded");

        forwardSignedLogs();

        System.out.println("Logs are forwarded");
    }

    private void forwardSignedLogs() throws RemoteException {
        // todo: sign
//        try {
//            byte[] input = new byte[0];
//            Signature sr = Signature.getInstance("SHA256withRSA");
//            sr.initSign(this.keyPair.getPrivate());
//            sr.update(input);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // save
        matchingServiceInterface.saveLogs(randomNumbers, hashes, timestamps, tokens);
    }
}
