package com.example.doctor;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;

import com.example.matchingservice.MatchingServiceInterface;


public class DoctorController {

    private Stage stage;
    private Doctor doctor;
    private String phoneNumber;

    public Button release;
    public TextArea pn;

    public void initialize(Stage stage) {

        this.stage = stage;
        startServer();
    }

    public void startServer() {
        Registry matchingService;
        MatchingServiceInterface matchingServiceInterface;
        try {
            matchingService = LocateRegistry.getRegistry("localhost", 5000);
            matchingServiceInterface = (MatchingServiceInterface) matchingService.lookup("MatchingServiceImpl");

            this.doctor = new Doctor(matchingServiceInterface);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void handleButtonGetLogs() throws IOException, NoSuchAlgorithmException {

        String phoneNumber =  pn.getText();
        try {
            doctor.getLogsFromUser(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}