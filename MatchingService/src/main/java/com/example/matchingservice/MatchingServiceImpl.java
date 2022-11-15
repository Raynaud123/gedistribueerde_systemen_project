package com.example.matchingservice;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;

public class MatchingServiceImpl extends UnicastRemoteObject implements MatchingServiceInterface{

    MatchingService matchingService = new MatchingService();

    protected MatchingServiceImpl() throws RemoteException {
    }

    @Override
    public void flushCapsules(String hex, Timestamp timeInterval, String userToken) {
            matchingService.addCapsule(hex,timeInterval,userToken);
    }
}
