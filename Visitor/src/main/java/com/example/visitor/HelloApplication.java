package com.example.visitor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    Visitor visitor;

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Momenteel verplaatst naar RegisterController
        //Voorlopige plaats
/*        Registry mixingProxy;
        MixingProxyInterface mixingProxyInterface;
        try {
            mixingProxy = LocateRegistry.getRegistry("localhost", 4500);
            mixingProxyInterface = (MixingProxyInterface) mixingProxy.lookup("MixingProxyImpl");

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        visitor = new Visitor(mixingProxyInterface);*/
        //Einde voorlopig


        // QRcode.fxml wordt nu in RegisterController opgeroepen, maar ik laat de if staan, misschien is hij later nog nodig
        if(visitor==null){
            FXMLLoader loader = new FXMLLoader((Objects.requireNonNull(HelloApplication.class.getResource("Register.fxml"))));
            primaryStage.setTitle("Register view");
            primaryStage.setScene(new Scene(loader.load()));
            RegisterController registerController = loader.getController();
            registerController.initialize();
            primaryStage.show();

        }else{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Qrcode.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            QRCodeController controller = loader.getController();
            controller.initData(visitor,primaryStage);

            primaryStage.show();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }


}