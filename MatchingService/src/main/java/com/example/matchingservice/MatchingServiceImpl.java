package com.example.matchingservice;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.sql.Timestamp;
import java.util.ArrayList;

public class MatchingServiceImpl extends UnicastRemoteObject implements MatchingServiceInterface {

    MatchingService matchingService;

    protected MatchingServiceImpl(MatchingService matchingService) throws RemoteException {
        this.matchingService = matchingService;
    }

    @Override
    public void flushCapsules(String hash, Timestamp timeInterval, String userToken) {
        matchingService.addCapsule(hash,timeInterval,userToken);
    }

    public void saveLogs(ArrayList<Integer> randomNumbers, byte[] randomNumbersSigned, ArrayList<String> hashes, byte[] hashesSigned, ArrayList<Timestamp> timestamps, byte[] timestampsSigned, ArrayList<String> tokens, byte[] tokensSigned) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        matchingService.saveInfectedLogs(randomNumbers, randomNumbersSigned, hashes, hashesSigned, timestamps, timestampsSigned, tokens, tokensSigned);
    }

    @Override
    public ArrayList<String> fetchCriticalHashes() throws RemoteException {
        return matchingService.fetchCriticalHashes();
    }

    @Override
    public ArrayList<Timestamp> fetchCriticalTimeInterval() throws RemoteException {
        return matchingService.fetchCriticalTimeInterval();
    }

    @Override
    public void sendInformedToken(String token) throws RemoteException {
        matchingService.setTokenToInformed(token);
    }

    public void setPublicKey(PublicKey publicKey) throws RemoteException {
        matchingService.setPublicKey(publicKey);
    }

}
