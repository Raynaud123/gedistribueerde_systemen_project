package com.example.visitor;

import com.example.matchingservice.MatchingServiceInterface;
import com.example.mixingproxy.MixingProxyInterface;

import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.security.*;
import java.sql.Timestamp;
import java.util.*;

public class Visitor {
    private final ArrayList<Integer> randomNumbers;
    private final ArrayList<String> CFs;
    private final ArrayList<String> hashes;
    private final ArrayList<Timestamp> timestamps;
    private final ArrayList<String> tokens;
    private ArrayList<String> tokensOfToday;
    private final ArrayList<String> signatures;
    private ArrayList<String> signaturesOfToday;
    private final Map<String,String> mappingResTo;
    private final PublicKey publicKeyMixing;
    private final PublicKey publicKeyRegistrar;
    private String currentHash;
    private String currentToken;
    private final String phoneNumber;
    private int currentIndex;

    private final MixingProxyInterface mixingProxyInterface;
    private final MatchingServiceInterface matchingServiceInterface;


    public Visitor(String phoneNumber, MixingProxyInterface mixingProxyInterface, MatchingServiceInterface matchingServiceInterface, PublicKey publicKeyRegistrar) throws RemoteException {
        this.mixingProxyInterface = mixingProxyInterface;
        this.CFs = new ArrayList<>();
        this.hashes = new ArrayList<>();
        this.randomNumbers = new ArrayList<>();
        this.timestamps = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.tokensOfToday = new ArrayList<>();
        this.signatures = new ArrayList<>();
        this.signaturesOfToday = new ArrayList<>();
        mappingResTo = new HashMap<>();
        this.phoneNumber = phoneNumber;
        this.matchingServiceInterface = matchingServiceInterface;
        publicKeyMixing = mixingProxyInterface.getPublicKey();
        this.publicKeyRegistrar = publicKeyRegistrar;
        currentIndex = -1;
    }

    public BufferedImage addCapsuleInformation(int randomNumber, String cateringFacility, String hashString, Timestamp ts) throws RemoteException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        randomNumbers.add(randomNumber);
        CFs.add(cateringFacility);
        currentHash = hashString;
        hashes.add(hashString);
        timestamps.add(ts);
        currentToken = tokensOfToday.get(0);
        tokensOfToday.remove(currentToken);
        tokens.add(currentToken);
        currentIndex++;
        String signature = signaturesOfToday.get(currentIndex);
        String signedHash = mixingProxyInterface.receiveCapsule(hashString, ts, currentToken, signature);
        if (hashStringCheck(signedHash,hashString)){
            mappingResTo.put(hashString,currentToken);
            return IconGenVisitor.generateIdenticons(hashString, 300,300);
        }
        else {
            return  null;
        }
    }

    private boolean hashStringCheck(String signedHash, String hashString) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("NONEwithRSA");
        signature.initVerify(publicKeyMixing);
        byte[] messageBytes = Base64.getDecoder().decode(hashString);
        byte[] signedBytes  = Base64.getDecoder().decode(signedHash);


        signature.update(messageBytes);
        return signature.verify(signedBytes);
    }

    public void flushCapsules() throws RemoteException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        timestamps.add(ts);
        mixingProxyInterface.flushCapsules(currentHash, currentToken, ts, signatures.get(currentIndex));
    }

    public void setTokens(Map<String, ArrayList<String>> tokensAndSignaturesOfToday) {
        this.tokensOfToday = tokensAndSignaturesOfToday.get("tokens");
        currentIndex=-1;
        this.signaturesOfToday = tokensAndSignaturesOfToday.get("signatures");
        signatures.addAll(signaturesOfToday);

        System.out.println("Tokens are set");
    }

    public void releaseLogs() {
        ReleaseLogsToFile.writeJsonFile(phoneNumber, randomNumbers, hashes, timestamps, tokens);
    }

    public Boolean fetchCritical() throws RemoteException {
        ArrayList<String> criticalHashes = matchingServiceInterface.fetchCriticalHashes();
        ArrayList<Timestamp> criticalTimestamps = matchingServiceInterface.fetchCriticalTimeInterval();
        ArrayList<Integer> indexHashes = new ArrayList<>();
        ArrayList<Integer> indexCriticalHashes = new ArrayList<>();
        boolean gevonden = false;

        for(int i = 0; i < hashes.size(); i++){
            for(int j = 0; j < criticalHashes.size(); j++){
                if (hashes.get(i).equals(criticalHashes.get(j))){
                    indexHashes.add(i);
                    indexCriticalHashes.add(j);
                }
            }
        }

        for(int i = indexHashes.size() -1; i >= 0; i--){
            if(!criticalTimestamps.get(indexCriticalHashes.get(i)).toString().equals(timestamps.get(indexHashes.get(i)).toString())){
                indexHashes.remove(i);
                indexCriticalHashes.remove(i);
            }
        }

        for (int indexHash : indexHashes) {
            if (mappingResTo.containsKey(hashes.get(indexHash))) {
                mixingProxyInterface.sendInformedToken(mappingResTo.get(hashes.get(indexHash)));
                gevonden = true;
            }
        }
        return gevonden;
    }

    public void logout() {
        currentHash=null;
        currentToken=null;
    }
}
