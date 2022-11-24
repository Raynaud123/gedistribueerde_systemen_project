package com.example.registrar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.security.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Visitor {

    // Unique ID is a phone number
    private final String phoneNumber;
    private final ObservableMap<LocalDate, ArrayList<String>> tokensMap;
    private final KeyPair keyPair;


    public Visitor(String phoneNumber) throws NoSuchAlgorithmException {
        this.phoneNumber = phoneNumber;
        tokensMap = FXCollections.observableMap(new HashMap<>());
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        this.keyPair = keyPairGenerator.generateKeyPair();
    }

    // generates tokens for 30 days
    public PublicKey generateTokens() throws NoSuchAlgorithmException {

        for (LocalDate date = LocalDate.now(); date.isBefore(LocalDate.now().plusDays(30)); date = date.plusDays(1)) {

            ArrayList<String> tokensOfOneDay = new ArrayList<>();

            // 48 tokens a day
            for (int i = 0; i < 48; i++) {
                try {
                    // Sign(random,dayi)
                    String input = Integer.toString(ThreadLocalRandom.current().nextInt()) + date;

                    // Create signature
                    Signature sr = Signature.getInstance("SHA256withRSA");
                    sr.initSign(this.keyPair.getPrivate());
                    sr.update(input.getBytes());
                    tokensOfOneDay.add(Arrays.toString(sr.sign()));
                } catch (InvalidKeyException | SignatureException e) {
                    throw new RuntimeException(e);
                }
            }

            tokensMap.put(date, tokensOfOneDay);
        }
        return keyPair.getPublic();
    }

    public ArrayList<String> getTokensOfToday() {
        try {
            return tokensMap.get(LocalDate.now());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ObservableMap<LocalDate, ArrayList<String>> getTokensMap() {
        return tokensMap;
    }
}
