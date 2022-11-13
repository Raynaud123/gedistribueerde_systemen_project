package com.example.visitor;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class HelloApplication extends Application {

    private Visitor visitor = new Visitor();

    private CheckBox cbAskConfirmation;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    private ImageView ivCameraOutput;
    private QRDecoder qrDecoder = new QRDecoder();

    private Webcam webcam;
    private boolean showingDialog = false;
    private long lastDirectlyOpenedTime = 0;



    @Override
    public void start(Stage primaryStage) throws IOException {

        if(visitor==null){
            Parent root =  FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Register.fxml")));
            primaryStage.setTitle("Register view");

            primaryStage.setScene(new Scene(root, 1000,700));
            primaryStage.show();

        }else{
            Parent root =  FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("QRcode.fxml")));
            primaryStage.setTitle("QRcode view");
            primaryStage.setScene(new Scene(root, 1000,700));
            primaryStage.show();
        }




//       primaryStage.setScene(makeUI());
//        startCameraInput();
    }

    public static void main(String[] args) {
        launch(args);
    }


}