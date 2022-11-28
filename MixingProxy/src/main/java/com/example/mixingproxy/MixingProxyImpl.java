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
    public String receiveCapsule(String hashString, Timestamp ts, String token, String signature) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return mixingProxy.receive(hashString, ts, token, signature);
    }


    @Override
    public void flushCapsules(String hash, String token, Timestamp timestamp, String signature) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        mixingProxy.receive(hash, timestamp, token, signature);
    }

    @Override
    public PublicKey getPublicKey() {
        return mixingProxy.getPublicKey();
    }

    @Override
    public void sendInformedToken(String token) throws RemoteException {
        mixingProxy.sendInformedToken(token);
    }
}
