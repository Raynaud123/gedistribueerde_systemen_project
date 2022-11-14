package com.example.BarOwner;

import com.example.registrar.RegistrarInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) throws RemoteException {
        Registry registrarRegistry;
        RegistrarInterface registrarInterface;
        try {
            registrarRegistry = LocateRegistry.getRegistry("localhost", 1112);
            registrarInterface = (RegistrarInterface) registrarRegistry.lookup("RegistrarService");

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

        BarOwnerMenu.showMenu(registrarInterface);
    }
}
