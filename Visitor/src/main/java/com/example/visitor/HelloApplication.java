package com.example.visitor;

import com.example.mixingproxy.MixingProxyInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class HelloApplication extends Application {




    @Override
    public void start(Stage primaryStage) throws IOException {

        //Voorlopige plaats
        Registry mixingProxy;
        MixingProxyInterface mixingProxyInterface;
        try {
            mixingProxy = LocateRegistry.getRegistry("localhost", 4500);
            mixingProxyInterface = (MixingProxyInterface) mixingProxy.lookup("MixingProxyImpl");

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        Visitor visitor = new Visitor(mixingProxyInterface);
        //Einde voorlopig

        if(visitor==null){
            Parent root =  FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("Register.fxml")));
            primaryStage.setTitle("Register view");

            primaryStage.setScene(new Scene(root, 1000,700));
            primaryStage.show();

        }else{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Qrcode.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            QRCodeController controller = loader.getController();
            controller.initData(visitor,loader,primaryStage);

            primaryStage.show();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }


}