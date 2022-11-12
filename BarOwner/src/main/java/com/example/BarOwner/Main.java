package com.example.BarOwner;

import com.example.registrar.RegistrarInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws RemoteException {
        Registry registrarRegistry;
        RegistrarInterface registrarInterface;
        try {
            registrarRegistry = LocateRegistry.getRegistry("localhost", 1111);
            registrarInterface = (RegistrarInterface) registrarRegistry.lookup("RegistrarService");

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

        BarOwner barOwner = new BarOwner("CF", "location", registrarInterface);
        barOwner.register();
        barOwner.getPseudonymBatch(LocalDate.now(), LocalDate.now());
        barOwner.generateQRCode(LocalDate.now());

    }
}
