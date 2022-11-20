package com.example.matchingservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;

public interface MatchingServiceInterface extends Remote {

    void flushCapsules(String hash, Timestamp timeInterval, String userToken) throws RemoteException;
}
