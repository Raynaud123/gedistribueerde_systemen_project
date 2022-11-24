package com.example.matchingservice;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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

    public void saveLogs(ArrayList<Integer> randomNumbers, ArrayList<String> hashes, ArrayList<Timestamp> timestamps, ArrayList<String> tokens) {
        matchingService.saveInfectedLogs(randomNumbers, hashes, timestamps, tokens);
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
}
