package com.example.doctor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DoctorGui extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader((Objects.requireNonNull(DoctorGui.class.getResource("doctor-view.fxml"))));
        stage.setTitle("Doctor view");
        stage.setScene(new Scene(loader.load()));
        DoctorGuiController doctorGuiController = loader.getController();
        doctorGuiController.initialize(stage);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}