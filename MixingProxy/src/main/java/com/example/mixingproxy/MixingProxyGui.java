package com.example.mixingproxy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MixingProxyGui extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MixingProxyGui.class.getResource("mixingproxy-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mixing proxy");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
