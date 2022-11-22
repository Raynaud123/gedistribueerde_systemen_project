package com.example.visitor;

import com.example.mixingproxy.MixingProxyInterface;
import com.example.registrar.RegistrarInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController {

    private Stage stage;

    public Button signup;
    public TextArea pn;
    public Registry registrarRegistry;
    public RegistrarInterface registrarInterface;
    public Visitor visitor;


    public void initialize(Stage stage) {

        this.stage = stage;
        try {
            registrarRegistry = LocateRegistry.getRegistry("localhost", 1112);
            registrarInterface = (RegistrarInterface) registrarRegistry.lookup("RegistrarService");

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleButtonSignUp() throws IOException, NoSuchAlgorithmException {

        String phoneNumber =  pn.getText();
        if (isValidNumber(phoneNumber)) {
            // Register visitor in Registrar
            registrarInterface.registerVisitor(phoneNumber);

            // RMI mixing proxy
            Registry mixingProxy;
            MixingProxyInterface mixingProxyInterface;
            try {
                mixingProxy = LocateRegistry.getRegistry("localhost", 4500);
                mixingProxyInterface = (MixingProxyInterface) mixingProxy.lookup("MixingProxyImpl");

            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }

            // Register visitor in Visitor
            visitor = new Visitor(phoneNumber, mixingProxyInterface);

            // Set tokens of today in Visitor\Visitor
            visitor.setTokens(registrarInterface.getTokensOfToday(phoneNumber));

            // Switch scene
            switchToQRScene();
        }
        else {
            Alert errorDialog = new Alert(Alert.AlertType.ERROR);
            errorDialog.setTitle("Invalid Number");
            errorDialog.setHeaderText("You entered an invalid phone number, please try again");
            errorDialog.show();
        }
    }

    private boolean isValidNumber(String phoneNumber) {
        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(phoneNumber);
        return (m.matches());
    }

    public void switchToQRScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Qrcode.fxml"));
        stage.getScene().setRoot(loader.load());

        QRCodeController controller = loader.getController();
        controller.initData(visitor, stage);

        stage.show();
    }

}