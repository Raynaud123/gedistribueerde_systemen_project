package com.example.visitor;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Map;

public interface RegistrarInterface {
    void registerVisitor(String user) throws RemoteException;

}
