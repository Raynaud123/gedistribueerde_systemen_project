package com.example.mixingproxy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;

public interface MatchingServiceInterface extends Remote {

    void flushCapsules(String hash, Timestamp timeInterval, String userToken) throws RemoteException;

    void saveLogs(ArrayList<Integer> randomNumbers, ArrayList<String> hashes, ArrayList<Timestamp> timestamps, ArrayList<String> tokens) throws RemoteException;

    ArrayList<String> fetchCriticalHashes() throws RemoteException;

    ArrayList<Timestamp> fetchCriticalTimeInterval() throws RemoteException;

    void sendInformedToken(String token) throws RemoteException;
}
