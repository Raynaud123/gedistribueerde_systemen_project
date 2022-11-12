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

    private Visitor visitor;

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

    private Scene makeUI() {
        BorderPane root = new BorderPane();

        // Bottom controls
        FlowPane bottomBar = new FlowPane();
        bottomBar.setPrefHeight(56);
        bottomBar.setHgap(16);
        bottomBar.setVgap(16);
        bottomBar.setOrientation(Orientation.HORIZONTAL);
        bottomBar.setAlignment(Pos.CENTER);

        // Ask for confirmation checkbox
        cbAskConfirmation = new CheckBox();
        cbAskConfirmation.setText("Pedir confirmación antes de abrir la carpeta escaneada");
        bottomBar.getChildren().add(cbAskConfirmation);

        // Camera container
        BorderPane webCamPane = new BorderPane();
        webCamPane.setPrefHeight(500);
        webCamPane.setPrefWidth(600);
        webCamPane.setStyle("-fx-background-color: #CCC;");
        ivCameraOutput = new ImageView();
        webCamPane.setCenter(ivCameraOutput);

        // Setup image view for camera capture
        ivCameraOutput.setFitHeight(500);
        ivCameraOutput.setFitWidth(600);
        ivCameraOutput.prefHeight(500);
        ivCameraOutput.prefWidth(600);
        ivCameraOutput.setPreserveRatio(true);

        // Assembly ui
        root.setCenter(webCamPane);
        root.setBottom(bottomBar);
        return new Scene(root);
    }

    private void startCameraInput() {
        Task<Void> webCamTask = new Task<Void>() {
            @Override
            protected Void call() {
                webcam = Webcam.getDefault();
                webcam.open();
                startWebCamStream();

                return null;
            }
        };

        Thread webCamThread = new Thread(webCamTask);
        webCamThread.setDaemon(true);
        webCamThread.start();
    }

    private void startWebCamStream() {
        boolean stopCamera = false;

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() {
                final AtomicReference<WritableImage> ref = new AtomicReference<>();
                BufferedImage img;

                //noinspection ConstantConditions,LoopConditionNotUpdatedInsideLoop
                while (!stopCamera) {
                    try {
                        if ((img = webcam.getImage()) != null) {
                            ref.set(SwingFXUtils.toFXImage(img, ref.get()));
                            img.flush();
                            Platform.runLater(() -> imageProperty.set(ref.get()));

                            String scanResult = qrDecoder.decodeQRCode(img);
                            if (scanResult != null) {
                                onQrResult(scanResult);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        ivCameraOutput.imageProperty().bind(imageProperty);
    }

    private void onQrResult(String scanResult) {
        Platform.runLater(() -> {
            if (cbAskConfirmation.isSelected()) {
                if (!showingDialog) {
                    showingDialog = true;

                    Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmDialog.setTitle("Código QR detectado");
                    confirmDialog.setHeaderText("¿Desea abrir la carpeta: '" + scanResult + "'?");

                    ButtonType yes = new ButtonType("Abrir");
                    ButtonType no = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
                    confirmDialog.getButtonTypes().setAll(no, yes);
                    Optional<ButtonType> result = confirmDialog.showAndWait();
                    if (result.isPresent() && result.get() == yes) {
                        openFolderInExplorer(scanResult);
                    }

                    showingDialog = false;
                }
            } else {
                long currentTime = new Date().getTime();
                if (lastDirectlyOpenedTime == 0 || (currentTime - lastDirectlyOpenedTime)/1000 > 3) {
                    lastDirectlyOpenedTime = currentTime;
                    openFolderInExplorer(scanResult);
                }
            }
        });
    }

    private void openFolderInExplorer(String url) {
        File directory = new File(url);
        if (!directory.isDirectory()) {
            Alert errorDialog = new Alert(Alert.AlertType.ERROR);
            errorDialog.setTitle("Error");
            errorDialog.setHeaderText("QR code niet gevonden");
            errorDialog.showAndWait();
            return;
        }

        if (isWindows()) {
            try {
                Runtime.getRuntime().exec("explorer.exe " + url);
            } catch (IOException e) {
                System.err.println("Given url could not be opened");
                e.printStackTrace();
            }
        } else {
            try {
                ProcessBuilder builder = new ProcessBuilder("sh", "-c", "xdg-open " + url);
                builder.start();
            } catch (IOException e) {
                System.err.println("Given url could not be opened");
                e.printStackTrace();
            }
        }
    }

    private boolean isWindows() {
        String OS = System.getProperty("os.name").toLowerCase();
        return (OS.contains("win"));
    }

}