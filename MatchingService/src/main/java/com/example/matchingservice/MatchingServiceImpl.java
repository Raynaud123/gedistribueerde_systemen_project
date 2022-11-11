package com.example.matchingservice;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MatchingServiceImpl extends UnicastRemoteObject implements MatchingServiceInterface{

    protected MatchingServiceImpl() throws RemoteException {
    }
}
