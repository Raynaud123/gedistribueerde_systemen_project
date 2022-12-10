package com.example.doctor;

import com.example.matchingservice.MatchingServiceInterface;

import java.rmi.RemoteException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;

public class Doctor {

    private ArrayList<Integer> randomNumbers;
    private ArrayList<String> hashes;
    private ArrayList<Timestamp> timestamps;
    private ArrayList<String> tokens;

    MatchingServiceInterface matchingServiceInterface;
    private final KeyPair keyPair;



    public Doctor(MatchingServiceInterface matchingServiceInterface) throws NoSuchAlgorithmException, RemoteException {
        this.matchingServiceInterface = matchingServiceInterface;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        this.keyPair = keyPairGenerator.generateKeyPair();
        matchingServiceInterface.setPublicKey(keyPair.getPublic());
    }

    public void getLogsFromUser(String phoneNumber) throws ParseException {
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

    private void forwardSignedLogs() {

        try {
            byte[] randomNumbersByte = randomNumbers.toString().getBytes();
            byte[] hashesByte = hashes.toString().getBytes();
            byte[] timestampsByte = timestamps.toString().getBytes();
            byte[] tokensByte = tokens.toString().getBytes();

            Signature srRandomNumbers = Signature.getInstance("SHA256withRSA");
            Signature srHashes = Signature.getInstance("SHA256withRSA");
            Signature srTimestamps = Signature.getInstance("SHA256withRSA");
            Signature srTokens = Signature.getInstance("SHA256withRSA");

            srRandomNumbers.initSign(this.keyPair.getPrivate());
            srHashes.initSign(this.keyPair.getPrivate());
            srTimestamps.initSign(this.keyPair.getPrivate());
            srTokens.initSign(this.keyPair.getPrivate());

            srRandomNumbers.update(randomNumbersByte);
            srHashes.update(hashesByte);
            srTimestamps.update(timestampsByte);
            srTokens.update(tokensByte);

            byte[] randomNumbersSigned = srRandomNumbers.sign();
            byte[] hashesSigned = srHashes.sign();
            byte[] timestampsSigned = srTimestamps.sign();
            byte[] tokensSigned = srTokens.sign();

            // save
            matchingServiceInterface.saveLogs(randomNumbers, randomNumbersSigned, hashes, hashesSigned, timestamps, timestampsSigned, tokens, tokensSigned);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
