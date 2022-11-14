package com.example.BarOwner;

import com.example.registrar.RegistrarInterface;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BarOwner {
    public final String CF;
    public final String location;

    private final Map<LocalDate, byte[]> pseudonymMap;
    private final Map<LocalDate, Integer> randomMap;

    private final RegistrarInterface registrarInterface;

    public BarOwner(String CF, String location, RegistrarInterface registrarInterface) {
        this.CF = CF;
        this.location = location;
        this.pseudonymMap = new HashMap<>();
        this.randomMap = new HashMap<>();
        this.registrarInterface = registrarInterface;
    }

    public void register() throws RemoteException {
        registrarInterface.register(CF, location);
    }

    public void getPseudonymBatch(LocalDate startDate, LocalDate endDate) throws RemoteException {
        Map<LocalDate, byte[]> pseudonyms = registrarInterface.getPseudonyms(CF, location, startDate, endDate);
        if (pseudonyms == null) {
            System.out.println("CF not registered at registrar");
            return;
        }

        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            if (pseudonymMap.containsKey(date)) {
                continue;
            }
            pseudonymMap.put(date, pseudonyms.get(date));
        }
    }

    public void generateQRCode(LocalDate date) {
        if (!pseudonymMap.containsKey(date)) {
            System.out.println("No pseudonymn available for this date");
            return;
        }

        int randomNum;
        if (!randomMap.containsKey(date)) {
            randomNum = ThreadLocalRandom.current().nextInt();
            randomMap.put(date, randomNum);
        }
        else {
            randomNum = randomMap.get(date);
        }

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("sha-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(randomNum);
            outputStream.write(pseudonymMap.get(date));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] hash = digest.digest(outputStream.toByteArray());
        System.out.println("Base 64 hash: " + Base64.getEncoder().encodeToString(hash));
        System.out.println("randomNum: " + randomNum);
        System.out.println("CF: " + CF);

        String QRContent = randomNum + " " + CF + " " + Base64.getEncoder().encodeToString(hash);

        try {
            byte[] QRCode = QRCodeGenerator.getQRCodeImage(QRContent, 300, 300);
            System.out.println("Base 64 of byte array of QR code: " + Base64.getEncoder().encodeToString(QRCode));

            QRCodeGenerator.generateQRCodeImage(QRContent, 300, 300, "BarOwner/src/main/resources/" + CF + "_" + location + "_" + date+ ".png");
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }


    }

}
