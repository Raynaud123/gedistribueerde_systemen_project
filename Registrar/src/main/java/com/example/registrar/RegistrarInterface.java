package com.example.registrar;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public interface RegistrarInterface extends Remote {
    void registerCF(String CF, String location) throws RemoteException;

    Map<LocalDate, byte[]> getPseudonyms(String CF, String location, LocalDate startDate, LocalDate endDate) throws RemoteException;

    PublicKey registerVisitor(String phoneNumber) throws RemoteException, NoSuchAlgorithmException;

    Map<String, ArrayList<String>> getTokensOfToday(String phoneNumber) throws RemoteException;

    void sentUninformedTokens(ArrayList<String> temp) throws RemoteException;

    boolean numberAlreadyExists(String phoneNumber) throws RemoteException;

    PublicKey getPublicKey() throws RemoteException;
}
