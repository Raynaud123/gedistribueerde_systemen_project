package com.example.matchingservice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MatchingServiceGui extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MatchingServiceGui.class.getResource("matchingservice-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Matching service");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
