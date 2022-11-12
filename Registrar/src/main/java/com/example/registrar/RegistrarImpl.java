package com.example.registrar;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.Map;

public class RegistrarImpl extends UnicastRemoteObject implements RegistrarInterface {

    Registrar registrar = new Registrar();

    protected RegistrarImpl() throws RemoteException {
    }

    @Override
    public void register(String CF, String location) throws RemoteException{
        registrar.registerCF(CF, location);
    }

    @Override
    public Map<LocalDate, byte[]> getPseudonyms(String CF, String location, LocalDate startDate, LocalDate endDate) throws RemoteException{
        return registrar.getPseudonyms(CF, location, startDate, endDate);
    }
}
