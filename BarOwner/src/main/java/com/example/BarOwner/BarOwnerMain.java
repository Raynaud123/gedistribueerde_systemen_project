package com.example.BarOwner;

import com.example.registrar.RegistrarInterface;
import com.example.registrar.SslClientSocketFactory;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class BarOwnerMain {
    public static void main(String[] args) throws RemoteException {
        Registry registrarRegistry;
        RegistrarInterface registrarInterface;
        try {
            SslClientSocketFactory csf = new SslClientSocketFactory("client", "clientpw");
            registrarRegistry = LocateRegistry.getRegistry("localhost", 3000, csf);
            registrarInterface = (RegistrarInterface) registrarRegistry.lookup("RegistrarService");

        } catch (NotBoundException | UnrecoverableKeyException | CertificateException | IOException |
                 NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }

        BarOwnerMenu.showMenu(registrarInterface);
    }
}
