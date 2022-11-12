package com.example.registrar;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Map;

public interface RegistrarInterface extends Remote {
    void register(String CF, String location) throws RemoteException;

    Map<LocalDate, byte[]> getPseudonyms(String CF, String location, LocalDate startDate, LocalDate endDate) throws RemoteException;
}
