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


    public Visitor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        tokensMap = FXCollections.observableMap(new HashMap<>());
    }

    // generates tokens for 30 days
    public void generateTokens() throws NoSuchAlgorithmException {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);

        Signature sr = Signature.getInstance("SHA256withRSA");

        for (LocalDate date = LocalDate.now(); date.isBefore(LocalDate.now().plusDays(30)); date = date.plusDays(1)) {

            ArrayList<String> tokensOfOneDay = new ArrayList<>();

            // 48 tokens a day
            for (int i = 0; i < 48; i++) {
                try {
                    // Sign(random,dayi)
                    String input = Integer.toString(ThreadLocalRandom.current().nextInt()) + date;

                    // Generate keypair
                    KeyPair keyPair = keyPairGenerator.generateKeyPair();

                    // Create signature
                    sr.initSign(keyPair.getPrivate());
                    sr.update(input.getBytes());
                    tokensOfOneDay.add(Arrays.toString(sr.sign()));
                } catch (InvalidKeyException | SignatureException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println(date + " " + tokensOfOneDay);
            tokensMap.put(date, tokensOfOneDay);
        }
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
