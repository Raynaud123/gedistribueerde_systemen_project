package com.example.BarOwner;

import com.example.registrar.RegistrarInterface;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BarOwnerMain {
    public static void main(String[] args) throws RemoteException {
        Registry registrarRegistry;
        RegistrarInterface registrarInterface;
        try {
            SslClientSocketFactory csf = new SslClientSocketFactory("client", "clientpw");

            registrarRegistry = LocateRegistry.getRegistry("localhost", 3000, csf);
            registrarInterface = (RegistrarInterface) registrarRegistry.lookup("RegistrarService");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        BarOwnerMenu.showMenu(registrarInterface);
    }
}
