package com.example.visitor;

import com.example.registrar.RegistrarInterface;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RegisterController {


    public Button signup;
    public TextArea pn;


    public void initialize() {
        Registry registrarRegistry;
        RegistrarInterface registrarInterface;
        try {
            registrarRegistry = LocateRegistry.getRegistry("localhost", 1112);
            registrarInterface = (RegistrarInterface) registrarRegistry.lookup("RegistrarService");

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleButtonSignUp(ActionEvent actionEvent) {
    }


}