package com.example.matchingservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;

public interface MatchingServiceInterface extends Remote {

    void flushCapsules(String hash, Timestamp timeInterval, String userToken) throws RemoteException;

    void saveLogs(ArrayList<Integer> randomNumbers, ArrayList<String> hashes, ArrayList<Timestamp> timestamps, ArrayList<String> tokens) throws RemoteException;
}
