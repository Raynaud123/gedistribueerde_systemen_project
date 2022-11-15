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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
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
   // MixingProxyInterface mixingProxyInterface;
    private Stage stage;
    private Scene scene;
    private Parent root;
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
    void initData(Visitor visitor, FXMLLoader loader, Stage primaryStage) {
        this.visitor = visitor;
        root = loader.getRoot();
        stage = primaryStage;
        scene = stage.getScene();
        //this.mixingProxyInterface = mixingProxyInterface;
    }

    public void handleButtondEnter(ActionEvent actionEvent) {
        randomNumber = rn.getText();
        cateringFacility = cf.getText();
        hashString = hash.getText();

        Node node = (Node) actionEvent.getSource();
        stage = (Stage) node.getScene().getWindow();

        if(Objects.equals(randomNumber, "") || Objects.equals(cateringFacility, "") || Objects.equals(hashString, "")){
            Alert errorDialog = new Alert(Alert.AlertType.ERROR);
            errorDialog.setTitle("Ingegeven waarden kloppen niet");
            errorDialog.setHeaderText("De tekst die u ingegeven hebt is niet volledig, probeer opnieuw");
            errorDialog.show();}
        else {
            try {
                int convertedRandomNumber = Integer.parseInt(randomNumber);
                Date date = new Date();
                Timestamp ts = new Timestamp(date.getTime());
                BufferedImage anwser = visitor.addCapsuleInformation(convertedRandomNumber,cateringFacility,hashString,ts);
                if (anwser == null){
                    Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                    errorDialog.setTitle("Er is iets misgelopen");
                    errorDialog.setHeaderText("Er is iets misgelopen probeer opnieuw");
                    errorDialog.show();
                }
                else {
                    switchToScene2(anwser);
                }
            }catch (NumberFormatException e){
                Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                errorDialog.setTitle("Random Number is geen getal");
                errorDialog.setHeaderText("Het random number die u ingegeven hebt is geen getal, probeer opnieuw");
                errorDialog.show();
            } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | IOException e) {
                e.printStackTrace();
            }
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
                if (scanResult != null) {
                    String[] arrOfStr = scanResult.split(" ", -1);
                    randomNumber = arrOfStr[0];
                    cateringFacility = arrOfStr[1];
                    hashString = arrOfStr[2];
                    try {
                        int convertedRandomNumber = Integer.parseInt(randomNumber);
                        Date date = new Date();
                        Timestamp ts = new Timestamp(date.getTime());
                        visitor.addCapsuleInformation(convertedRandomNumber,cateringFacility,hashString,ts);
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Icon.fxml"));


        stage.setScene(new Scene(loader.load()));

        IconController controller = loader.getController();
        controller.initData(visitor,loader,icon);

        stage.show();

    }
}
