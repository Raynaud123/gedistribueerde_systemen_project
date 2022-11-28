package com.example.mixingproxy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.sql.Timestamp;

public interface MixingProxyInterface  extends Remote {

    String receiveCapsule(String hashString, Timestamp ts, String token) throws RemoteException, NoSuchAlgorithmException, SignatureException, InvalidKeyException;

    void flushCapsules(String s, String s1, Timestamp timestamp) throws RemoteException, NoSuchAlgorithmException, SignatureException, InvalidKeyException;

    PublicKey getPublicKey() throws RemoteException;

    void sendInformedToken(String token) throws RemoteException;
}
