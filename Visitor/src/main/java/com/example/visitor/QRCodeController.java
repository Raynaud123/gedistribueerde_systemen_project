package com.example.visitor;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

public class QRCodeController{


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

    public Button tokensOfNewDay;

    private Visitor visitor;
    private Stage stage;
    private final QRDecoder qrDecoder = new QRDecoder();
    private ObjectProperty<Image> imageProperty;
    private Webcam webcam;
    private String randomNumber;
    private String cateringFacility;
    private String hashString;
    private Task<Void> webCamTask;
    private Task<Void> task;



    public void initData(Visitor visitor, Stage primaryStage) {
        this.visitor = visitor;
        stage = primaryStage;
        imageProperty = new SimpleObjectProperty<>();
        startCameraInput();
        TimerTask dailyTask = new TimerTask () {
            @Override
            public void run () {
                try {
                    fetchCritical();
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer ();
        timer.schedule(dailyTask, 1000*60*60*24, 1000*60*60*24);
    }

    public void handleButtonEnter(ActionEvent actionEvent) {
        randomNumber = rn.getText();
        cateringFacility = cf.getText();
        hashString = hash.getText();

        Node node = (Node) actionEvent.getSource();
        stage = (Stage) node.getScene().getWindow();

        if(Objects.equals(randomNumber, "") || Objects.equals(cateringFacility, "") || Objects.equals(hashString, "")){
            Alert errorDialog = new Alert(Alert.AlertType.ERROR);
            errorDialog.setTitle("Ingegeven waarden kloppen niet");
            errorDialog.setHeaderText("De tekst die u ingegeven hebt is niet volledig, probeer opnieuw");
            errorDialog.show();
        }
        else {
            try {
                int convertedRandomNumber = Integer.parseInt(randomNumber);
                Date date = new Date();
                Timestamp ts = new Timestamp(date.getTime());
                BufferedImage answer = visitor.addCapsuleInformation(convertedRandomNumber, cateringFacility, hashString, ts);
                if (answer == null) {
                    Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                    errorDialog.setTitle("Er is iets misgelopen");
                    errorDialog.setHeaderText("Er is iets misgelopen probeer opnieuw");
                    errorDialog.show();
                }
                else {
                    switchToScene2(answer);
                }
            } catch (NumberFormatException e) {
                Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                errorDialog.setTitle("Random Number is geen getal");
                errorDialog.setHeaderText("Het random number die u ingegeven hebt is geen getal, probeer opnieuw");
                errorDialog.show();
            } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleButtonReleaseLogs() {
        visitor.releaseLogs();
    }

    public void handleButtonTokensOfToday() throws RemoteException {
        visitor.setTokens(RegisterController.registrarInterface.getTokensOfToday(visitor.getPhoneNumber()));
    }


    private void startCameraInput() {

        webcam = Webcam.getDefault();


        if(webcam != null){
            webCamTask = new Task<>() {
                @Override
                protected Void call() {
                    webcam.open();
                    startWebCamStream();

                    return null;
                }
            };
            Thread webCamThread = new Thread(webCamTask);
            webCamThread.setDaemon(true);
            webCamThread.start();
        }
    }

    private void startWebCamStream() {
        boolean stopCamera = false;

        task = new Task<>() {

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
                if (scanResult != null) {
                    String[] arrOfStr = scanResult.split(" ", -1);
                    randomNumber = arrOfStr[0];
                    cateringFacility = arrOfStr[1];
                    hashString = arrOfStr[2];
                    try {
                        int convertedRandomNumber = Integer.parseInt(randomNumber);
                        Date date = new Date();
                        Timestamp ts = new Timestamp(date.getTime());
                        visitor.addCapsuleInformation(convertedRandomNumber, cateringFacility, hashString, ts);
                    }catch (NumberFormatException e){
                        Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                        errorDialog.setTitle("QR-code klopt niet");
                        errorDialog.setHeaderText("De QR code die u gescand heeft klopt niet");
                        errorDialog.show();
                    } catch (RemoteException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                }
        });
    }

    private void switchToScene2(BufferedImage icon) throws IOException {


        if(task != null){
            task.cancel();
        }
        webCamTask.cancel();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Icon.fxml"));
        stage.getScene().setRoot(loader.load());

        IconController controller = loader.getController();
        controller.initData(visitor,icon, stage);

        stage.show();
    }

    public void fetchCritical() throws IOException, ParseException {
        boolean gevonden = visitor.fetchCritical();

        if(gevonden){
            if(webcam.isOpen() && webcam != null){
                if(task != null){
                    task.cancel();
                }
                //webcam.close();
                if(webCamTask != null){
                    webCamTask.cancel();
                }

            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Infected.fxml"));
            stage.getScene().setRoot(loader.load());

            stage.show();
        }
    }
}
