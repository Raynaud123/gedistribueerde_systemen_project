package com.example.matchingservice;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;

public class MatchingServiceGuiController {

    MatchingService matchingService = new MatchingService();


    public TableView<Capsule> tableCapsules;
    public TableColumn<Timestamp, String> colTimestamp;
    public TableColumn<String, String> colUserToken;
    public TableColumn<String, String> colHash;
    public TableColumn<String, Boolean> colCrit;
    public TableColumn<String, Boolean> colInform;


    public void initialize() {
        startServer();
        observeCapsules();
    }

    public void startServer() {
        try {
            SslClientSocketFactory csf = new SslClientSocketFactory("client", "clientpw");
            SslServerSocketFactory ssf = new SslServerSocketFactory("registry", "registrypw");

            Registry registry = LocateRegistry.createRegistry(3001, csf, ssf);
            registry.bind("MatchingServiceService", new MatchingServiceImpl(matchingService));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Matching service server ready");
    }

    public void observeCapsules() {
        tableCapsules.setItems(matchingService.getCapsules());
        colTimestamp.setCellValueFactory(new PropertyValueFactory<>("timeInterval"));
        colUserToken.setCellValueFactory(new PropertyValueFactory<>("userToken"));
        colHash.setCellValueFactory(new PropertyValueFactory<>("hash"));
        colCrit.setCellValueFactory(new PropertyValueFactory<>("critical"));
        colInform.setCellValueFactory(new PropertyValueFactory<>("informed"));
    }

    public void sentUninformedTokens() throws RemoteException {
        matchingService.sentUninformedTokens();
    }
}
