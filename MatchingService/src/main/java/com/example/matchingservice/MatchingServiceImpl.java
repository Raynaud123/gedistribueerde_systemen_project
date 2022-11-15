package com.example.matchingservice;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;

public class MatchingServiceImpl extends UnicastRemoteObject implements MatchingServiceInterface{

    protected MatchingServiceImpl() throws RemoteException {
    }

    @Override
    public void flushCapsules(String hex, Timestamp timeInterval, String userToken) {

    }
}
