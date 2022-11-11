package com.example.mixingproxy;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MixingProxyImpl extends UnicastRemoteObject implements MixingProxyInterface {

    MixingProxy mixingProxy = new MixingProxy();

    MixingProxyImpl()throws RemoteException {
        super();
    }


    @Override
    public boolean receiveCapsule() {
        return false;
    }

    @Override
    public String sendBackSignedHas() {
        return null;
    }

    @Override
    public String flushCapsules() {
        return null;
    }
}
