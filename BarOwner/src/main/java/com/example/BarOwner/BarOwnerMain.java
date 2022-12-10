package com.example.BarOwner;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BarOwnerMain {
    public static void main(String[] args) throws RemoteException {
        Registry registrarRegistry;
        RegistrarInterface registrarInterface;
        try {
            registrarRegistry = LocateRegistry.getRegistry("localhost", 3000);
            registrarInterface = (RegistrarInterface) registrarRegistry.lookup("RegistrarService");

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

        BarOwnerMenu.showMenu(registrarInterface);
    }
}
