package com.example.visitor;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class QRCodeController implements Initializable {


    @FXML
    public ImageView cameraOutput;
    @FXML
    public Button confirmation;
    @FXML
    public TextArea rn;
    @FXML
    public TextArea cf;
    @FXML
    public TextArea hash;

    private Visitor visitor;
    private Stage stage;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    private QRDecoder qrDecoder = new QRDecoder();
    private Webcam webcam;
    private boolean showingDialog = false;
    private String randomNumber;
    private String cateringFacility;
    private String hashString;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startCameraInput();

    }

    public void handleButtondEnter(ActionEvent actionEvent) {
        randomNumber = rn.getText();
        cateringFacility = cf.getText();
        hashString = hash.getText();

        Node node = (Node) actionEvent.getSource();
        stage = (Stage) node.getScene().getWindow();
        visitor = (Visitor) stage.getUserData();

        if(Objects.equals(randomNumber, "") || Objects.equals(cateringFacility, "") || Objects.equals(hashString, "")){
            Alert errorDialog = new Alert(Alert.AlertType.ERROR);
            errorDialog.setTitle("Ingegeven waarden kloppen niet");
            errorDialog.setHeaderText("De tekst die u ingegeven hebt is niet volledig, probeer opnieuw");
            errorDialog.show();

        }else {
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            visitor.addCapsuleInformation(randomNumber,cateringFacility,hashString,ts);

        }
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

        cameraOutput.imageProperty().bind(imageProperty);
    }

    private void onQrResult(String scanResult) {
        Platform.runLater(() -> {
            //if (confirmation.isSelected()) {
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
//            } else {
//                long currentTime = new Date().getTime();
//                if (lastDirectlyOpenedTime == 0 || (currentTime - lastDirectlyOpenedTime)/1000 > 3) {
//                    lastDirectlyOpenedTime = currentTime;
//                    openFolderInExplorer(scanResult);
//                }
//            }
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
