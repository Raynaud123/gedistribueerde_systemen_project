package com.example.visitor;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    private Visitor visitor;


    @Override
    public void start(Stage primaryStage) throws IOException {

        if(visitor==null){
            Parent root =  FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Register.fxml")));
            primaryStage.setTitle("Register view");

            primaryStage.setScene(new Scene(root, 1000,700));
            primaryStage.show();

        }else{
            Parent root =  FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("QRcode.fxml")));
            primaryStage.setUserData(visitor);
            primaryStage.setTitle("QRcode view");
            primaryStage.setScene(new Scene(root, 1000,700));
            primaryStage.show();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }


}