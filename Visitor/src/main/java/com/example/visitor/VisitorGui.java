package com.example.visitor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class VisitorGui extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader((Objects.requireNonNull(VisitorGui.class.getResource("Register.fxml"))));
        stage.setTitle("Register view");
        stage.setScene(new Scene(loader.load()));
        RegisterController registerController = loader.getController();
        registerController.initialize(stage);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}