package com.example.registrar;

import java.security.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Visitor {

    // Unique ID is a phone number
    private final String phoneNumber;
    private final Map<LocalDate, ArrayList<String>> tokensMap;


    public Visitor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        tokensMap = new HashMap<>();
    }

    // generates tokens for 30 days
    public void generateTokens() throws NoSuchAlgorithmException {
        for (LocalDate date = LocalDate.now(); date.isBefore(LocalDate.now().plusDays(30)); date = date.plusDays(1)) {

            ArrayList<String> tokensOfOneDay = new ArrayList<>();

            // 48 tokens a day
            for (int i=0; i<48; i++) {
                try {
                    // Sign(random,dayi)
                    String input = Integer.toString((int) Math.floor(Math.random()*Integer.MAX_VALUE)) + date;

                    // Generate keypair
                    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                    keyPairGenerator.initialize(1024);
                    KeyPair keyPair = keyPairGenerator.generateKeyPair();

                    // Create signature
                    Signature sr = Signature.getInstance("SHA256withRSA");
                    sr.initSign(keyPair.getPrivate());
                    sr.update(input.getBytes());
                    tokensOfOneDay.add(Arrays.toString(sr.sign()));
                } catch (InvalidKeyException | SignatureException e) {
                    throw new RuntimeException(e);
                }
            }

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
}
