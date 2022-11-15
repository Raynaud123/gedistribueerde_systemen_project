package com.example.mixingproxy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;

public interface MixingProxyInterface  extends Remote {

    String receiveCapsule(int randomNumber, String cateringFacility, String hashString, Timestamp ts, String token) throws RemoteException;

    String flushCapsules() throws RemoteException;


}
