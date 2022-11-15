package com.example.mixingproxy;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.sql.Timestamp;

public class MixingProxyImpl extends UnicastRemoteObject implements MixingProxyInterface {

    MixingProxy mixingProxy;

    protected MixingProxyImpl(MixingProxy mixingProxy)throws RemoteException {
        this.mixingProxy = mixingProxy;
    }


    @Override
    public String receiveCapsule(String hashString, Timestamp ts, String token) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String signed = mixingProxy.receive(hashString,ts,token);
        return signed;
    }


    @Override
    public String flushCapsules(String s, String s1, Timestamp timestamp) {
        return null;
    }

    @Override
    public PublicKey getPublicKey() {
        return mixingProxy.getPublicKey();
    }
}
