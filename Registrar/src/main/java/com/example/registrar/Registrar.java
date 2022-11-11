package com.example.registrar;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Registrar {

    private final SecretKey masterKey;
    private final Map<String , CateringFacility> cateringFacilityMap;

    public Registrar() {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGenerator.init(256);
        this.masterKey = keyGenerator.generateKey();

        this.cateringFacilityMap = new HashMap<>();
    }

    public void registerCF(String CF, String location) {
        cateringFacilityMap.put(CF+location, new CateringFacility(CF, location));
    }

    public Map<LocalDate, byte[]> getPseudonyms(String CF, String location, LocalDate startDate, LocalDate endDate) {
        CateringFacility cateringFacility = cateringFacilityMap.get(CF+location);

        return cateringFacility.getPseudonyms(masterKey, startDate, endDate);
    }



}
