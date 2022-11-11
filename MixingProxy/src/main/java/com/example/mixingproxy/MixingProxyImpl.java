package com.example.mixingproxy;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MixingProxyImpl extends UnicastRemoteObject implements MixingProxyInterface {

    MixingProxy mixingProxy = new MixingProxy();

    MixingProxyImpl()throws RemoteException {
        super();
    }


    @Override
    public String receiveCapsule() {
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
