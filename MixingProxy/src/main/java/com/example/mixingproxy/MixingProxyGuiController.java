package com.example.mixingproxy;

import com.example.matchingservice.MatchingServiceInterface;
import com.example.registrar.RegistrarInterface;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;

public class MixingProxyGuiController {

    MixingProxy mixingProxy;

    public Button buttonFlush;

    public TableView<Capsule> tableCapsules;
    public TableColumn<Timestamp, String> colTimestamp;
    public TableColumn<String, String> colUserToken;
    public TableColumn<String, String> colHash;


    public void initialize() {
        startServer();
        observeCapsules();
    }

    public void startServer() {
        Registry matchingService;
        Registry registrar;
        MatchingServiceInterface matchingServiceInterface;
        RegistrarInterface registrarInterface;
        try {
            matchingService = LocateRegistry.getRegistry("localhost", 5000);
            matchingServiceInterface = (MatchingServiceInterface) matchingService.lookup("MatchingServiceImpl");

            registrar = LocateRegistry.getRegistry("localhost", 1112);
            registrarInterface = (RegistrarInterface) registrar.lookup("RegistrarService");

            this.mixingProxy = new MixingProxy(matchingServiceInterface, registrarInterface);

            Registry registry = LocateRegistry.createRegistry(4500);
            registry.rebind("MixingProxyImpl", new MixingProxyImpl(this.mixingProxy));
            System.out.println("Mixing proxy server ready");
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

    public void observeCapsules() {
        tableCapsules.setItems(mixingProxy.getCapsules());
        colTimestamp.setCellValueFactory(new PropertyValueFactory<>("timeInterval"));
        colUserToken.setCellValueFactory(new PropertyValueFactory<>("userToken"));
        colHash.setCellValueFactory(new PropertyValueFactory<>("hash"));
    }

    public void flushCapsules() {
        mixingProxy.flushCapsules();
    }

}
