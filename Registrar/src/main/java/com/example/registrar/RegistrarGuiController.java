package com.example.registrar;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RegistrarGuiController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}