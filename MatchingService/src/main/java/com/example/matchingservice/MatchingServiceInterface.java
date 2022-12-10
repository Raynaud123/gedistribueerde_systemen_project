package com.example.matchingservice;

import sun.misc.Signal;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.*;
import java.sql.Timestamp;
import java.util.ArrayList;

public interface MatchingServiceInterface extends Remote {

    void flushCapsules(String hash, Timestamp timeInterval, String userToken) throws RemoteException;

    void saveLogs(ArrayList<Integer> randomNumbers, byte[] randomNumbersSigned, ArrayList<String> hashes, byte[] hashesSigned, ArrayList<Timestamp> timestamps, byte[] timestampsSigned, ArrayList<String> tokens, byte[] tokensSigned) throws RemoteException, NoSuchAlgorithmException, SignatureException, InvalidKeyException;

    ArrayList<String> fetchCriticalHashes() throws RemoteException;

    ArrayList<Timestamp> fetchCriticalTimeInterval() throws RemoteException;

    void sendInformedToken(String token) throws RemoteException;

    void setPublicKey(PublicKey publicKey) throws RemoteException;
}
