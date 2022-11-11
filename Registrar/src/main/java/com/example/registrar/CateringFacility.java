package com.example.registrar;

import at.favre.lib.crypto.HKDF;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;

public class CateringFacility {

    private final String CF;
    private final String location;
    private final Map<LocalDate, SecretKey> secretKeyMap;
    private final Map<LocalDate, byte[]> pseudonymMap;

    public CateringFacility(String CF, String location) {
        this.CF = CF;
        this.location = location;
        this.secretKeyMap = new HashMap<>();
        this.pseudonymMap = new HashMap<>();
    }

    public void generateSecretKeys(SecretKey masterKey, LocalDate startDate, LocalDate endDate) {
        byte[] staticSalt32Byte = new byte[]{(byte) 0xDA, (byte) 0xAC, 0x3E, 0x10, 0x55, (byte) 0xB5, (byte) 0xF1, 0x3E, 0x53, (byte) 0xE4, 0x70, (byte) 0xA8, 0x77, 0x79, (byte) 0x8E, 0x0A, (byte) 0x89, (byte) 0xAE, (byte) 0x96, 0x5F, 0x19, 0x5D, 0x53, 0x62, 0x58, (byte) 0x84, 0x2C, 0x09, (byte) 0xAD, 0x6E, 0x20, (byte) 0xD4};

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            if (secretKeyMap.containsKey(date)) {
                continue;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                outputStream.write(masterKey.getEncoded());
                outputStream.write(CF.getBytes());
                outputStream.write(date.toString().getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            HKDF hkdf = HKDF.fromHmacSha256();
            byte[] pseudoRandomKey = hkdf.extract(staticSalt32Byte, outputStream.toByteArray());
            byte[] expandedAesKey = hkdf.expand(pseudoRandomKey, "aes-key".getBytes(), 16);
            SecretKey key = new SecretKeySpec(expandedAesKey, "AES");

            secretKeyMap.put(date, key);
        }
    }

    public Map<LocalDate, byte[]> getPseudonyms(SecretKey masterKey, LocalDate startDate, LocalDate endDate) {
        generateSecretKeys(masterKey, startDate, endDate);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("sha-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        Map<LocalDate, byte[]> pseudonyms = new HashMap<>();

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            if (pseudonymMap.containsKey(date)) {
                pseudonyms.put(date, pseudonymMap.get(date));
                continue;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                outputStream.write(secretKeyMap.get(date).getEncoded());
                outputStream.write(location.getBytes());
                outputStream.write(date.toString().getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            byte[] hash = digest.digest(outputStream.toByteArray());
            pseudonymMap.put(date, hash);

            pseudonyms.put(date, hash);
        }

        return pseudonyms;
    }
}
