package com.example.matchingservice;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;

public class MatchingServiceImpl extends UnicastRemoteObject implements MatchingServiceInterface {

    MatchingService matchingService;

    protected MatchingServiceImpl(MatchingService matchingService) throws RemoteException {
        this.matchingService = matchingService;
    }

    @Override
    public void flushCapsules(String hash, Timestamp timeInterval, String userToken) {
        matchingService.addCapsule(hash,timeInterval,userToken);
    }
}
