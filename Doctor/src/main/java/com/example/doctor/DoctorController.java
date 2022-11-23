package com.example.doctor;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class DoctorController {

    private Stage stage;
    private Doctor doctor;
    private String phoneNumber;

    public Button release;
    public TextArea pn;

    public void initialize(Stage stage) {

        this.stage = stage;
        doctor = new Doctor();
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