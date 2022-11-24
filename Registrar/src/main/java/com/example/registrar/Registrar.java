package com.example.registrar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Registrar {

    private final SecretKey masterKey;
    private final ObservableMap<String, CateringFacility> cateringFacilityMap;
    private final ObservableMap<String, Visitor> visitorMap;
    private final ObservableList<String> uninformedTokens;

    public Registrar() {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGenerator.init(256);
        this.masterKey = keyGenerator.generateKey();

        this.cateringFacilityMap = FXCollections.observableMap(new HashMap<>());
        this.visitorMap = FXCollections.observableMap(new HashMap<>());
        this.uninformedTokens = FXCollections.observableList(new ArrayList<>());
    }

    public void registerCF(String CF, String location) {
        cateringFacilityMap.put(CF+","+location, new CateringFacility(CF, location));
    }

    public Map<LocalDate, byte[]> getPseudonyms(String CF, String location, LocalDate startDate, LocalDate endDate) {
        if (!cateringFacilityMap.containsKey(CF+","+location)) {
            return null;
        }
        CateringFacility cateringFacility = cateringFacilityMap.get(CF+","+location);

        return cateringFacility.getPseudonyms(masterKey, startDate, endDate);
    }

    public ObservableMap<String, CateringFacility> getCateringFacilityMap() {
        return cateringFacilityMap;
    }

    public PublicKey registerVisitor(String phoneNumber) throws NoSuchAlgorithmException {
        visitorMap.put(phoneNumber, new Visitor(phoneNumber));
        return visitorMap.get(phoneNumber).generateTokens();
    }

    public ArrayList<String> getTokensOfToday(String phoneNumber) {
        return visitorMap.get(phoneNumber).getTokensOfToday();
    }

    public ObservableMap<String, Visitor> getVisitorMap() {
        return visitorMap;
    }

    public void sentUninformedTokens(ArrayList<String> temp) {
        uninformedTokens.addAll(temp);
    }
}
