package com.example.doctor;

import com.example.matchingservice.MatchingServiceInterface;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


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
            matchingService = LocateRegistry.getRegistry("localhost", 3001);
            matchingServiceInterface = (MatchingServiceInterface) matchingService.lookup("MatchingServiceService");

            this.doctor = new Doctor(matchingServiceInterface);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void handleButtonGetLogs() {
        String phoneNumber =  pn.getText();
        try {
            doctor.getLogsFromUser(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}