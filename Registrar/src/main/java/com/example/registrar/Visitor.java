package com.example.registrar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.security.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Visitor {

    // Unique ID is a phone number
    private final String phoneNumber;
    private final ObservableMap<LocalDate, ArrayList<String>> tokensMap;
    private final ObservableMap<LocalDate, ArrayList<String>> signatureMap;
    private final KeyPair keyPair;


    public Visitor(String phoneNumber, KeyPair keyPair) {
        this.phoneNumber = phoneNumber;
        tokensMap = FXCollections.observableMap(new HashMap<>());
        signatureMap = FXCollections.observableMap(new HashMap<>());
        this.keyPair = keyPair;
    }

    // generates tokens for 30 days
    public PublicKey generateTokens() throws NoSuchAlgorithmException {

        for (LocalDate date = LocalDate.now(); date.isBefore(LocalDate.now().plusDays(30)); date = date.plusDays(1)) {

            ArrayList<String> tokensOfOneDay = new ArrayList<>();
            ArrayList<String> signatureOfOneDay = new ArrayList<>();

            // 48 tokens a day
            for (int i = 0; i < 48; i++) {
                try {
                    // Sign(random,dayi)
                    String token = Integer.toString(ThreadLocalRandom.current().nextInt()) + date;
                    tokensOfOneDay.add(token);

                    // Create signature
                    Signature sr = Signature.getInstance("SHA256withRSA");
                    sr.initSign(this.keyPair.getPrivate());
                    sr.update(token.getBytes());
                    signatureOfOneDay.add(Base64.getEncoder().encodeToString(sr.sign()));
                } catch (InvalidKeyException | SignatureException e) {
                    throw new RuntimeException(e);
                }
            }

            tokensMap.put(date, tokensOfOneDay);
            signatureMap.put(date, signatureOfOneDay);
        }
        return keyPair.getPublic();
    }

    public Map<String, ArrayList<String>> getTokensOfToday() {
        try {
            Map<String, ArrayList<String>> mapWithTokensAndSignatures = new HashMap<>();
            mapWithTokensAndSignatures.put("tokens", this.tokensMap.get(LocalDate.now()));
            mapWithTokensAndSignatures.put("signatures", this.signatureMap.get(LocalDate.now()));
            return mapWithTokensAndSignatures;
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
