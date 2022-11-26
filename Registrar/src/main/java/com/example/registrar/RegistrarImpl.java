package com.example.registrar;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class RegistrarImpl extends UnicastRemoteObject implements RegistrarInterface {

    Registrar registrar;

    protected RegistrarImpl(Registrar registrar) throws RemoteException {
        this.registrar = registrar;
    }

    @Override
    public void registerCF(String CF, String location) throws RemoteException{
        registrar.registerCF(CF, location);
    }

    @Override
    public Map<LocalDate, byte[]> getPseudonyms(String CF, String location, LocalDate startDate, LocalDate endDate) throws RemoteException{
        return registrar.getPseudonyms(CF, location, startDate, endDate);
    }

    public PublicKey registerVisitor(String phoneNumber) throws RemoteException, NoSuchAlgorithmException {
        return registrar.registerVisitor(phoneNumber);
    }

    public ArrayList<String> getTokensOfToday(String phoneNumber) throws RemoteException {
        return registrar.getTokensOfToday(phoneNumber);
    }

    @Override
    public void sentUninformedTokens(ArrayList<String> temp) throws RemoteException {
        registrar.sentUninformedTokens(temp);
    }

    public boolean numberAlreadyExists(String phoneNumber) throws RemoteException {
        return registrar.numberAlreadyExists(phoneNumber);
    }



}
