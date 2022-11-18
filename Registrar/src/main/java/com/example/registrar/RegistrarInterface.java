package com.example.registrar;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public interface RegistrarInterface extends Remote {
    void registerCF(String CF, String location) throws RemoteException;

    Map<LocalDate, byte[]> getPseudonyms(String CF, String location, LocalDate startDate, LocalDate endDate) throws RemoteException;

    void registerVisitor(String phoneNumber) throws RemoteException, NoSuchAlgorithmException;

    ArrayList<String> getTokensOfToday(String phoneNumber) throws RemoteException;
}
