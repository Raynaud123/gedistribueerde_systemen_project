package com.example.mixingproxy;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;

public class MixingProxyImpl extends UnicastRemoteObject implements MixingProxyInterface {

    MixingProxy mixingProxy;

    protected MixingProxyImpl(MixingProxy mixingProxy)throws RemoteException {
        this.mixingProxy = mixingProxy;
    }


    @Override
    public String receiveCapsule(int randomNumber, String cateringFacility, String hashString, Timestamp ts, String token) {
        mixingProxy.receive(randomNumber,cateringFacility,hashString,ts,token);
        // Decrypt the signed Hash
        // Check validity
        // Add capsule
        return null;
    }


    @Override
    public String flushCapsules() {
        return null;
    }
}
