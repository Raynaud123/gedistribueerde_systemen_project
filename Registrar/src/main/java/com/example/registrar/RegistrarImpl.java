package com.example.registrar;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
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

    public void registerVisitor(String user) throws RemoteException {
        registrar.registerVisitor(user);
    }

}
