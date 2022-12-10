package com.example.registrar;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.*;

import javax.crypto.SecretKey;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RegistrarGuiController {

    Registrar registrar = new Registrar();

    public ComboBox<String> selectCF = new ComboBox<>();

    public Label labelCF;
    public Label labelLocation;

    public ObservableMap<LocalDate, SecretKey> currentSecretObservableMap;
    public ObservableList<Map.Entry<LocalDate, SecretKey>> dataSecret;
    public MapChangeListener<LocalDate, SecretKey> changeListenerSecret;

    public TableView<Map.Entry<LocalDate, SecretKey>> tableSecret;
    public TableColumn<Map.Entry<LocalDate, SecretKey>, String> colSecretDate;
    public TableColumn<Map.Entry<LocalDate, SecretKey>, String> colSecretKey;


    public ObservableMap<LocalDate, byte[]> currentPseudonymObservableMap;
    public ObservableList<Map.Entry<LocalDate, byte[]>> dataPseudonym;
    public MapChangeListener<LocalDate, byte[]> changeListenerPseudonym;

    public TableView<Map.Entry<LocalDate, byte[]>> tablePseudonym;
    public TableColumn<Map.Entry<LocalDate, byte[]>, String> colPseudonymDate;
    public TableColumn<Map.Entry<LocalDate, byte[]>, String> colPseudonymPseudonym;


    public ComboBox<String> selectVis = new ComboBox<>();
    public ComboBox<String> selectVisDate = new ComboBox<>();

    public Label labelPhone;

    public ObservableMap<LocalDate, ArrayList<String>> currentDateObservableMap;
    public MapChangeListener<LocalDate, ArrayList<String>> changeListenerDate;

    public ListView<String> listTokens;
    public ListView<String> listSign;


    public void initialize() {
        startServer();
        observeRegistrar();
        observeComboBoxCF();
        observeComboBoxVis();
    }

    public void startServer() {
        try {
            Registry registry = LocateRegistry.createRegistry(3000);
            registry.bind("RegistrarService", new RegistrarImpl(this.registrar));
            System.out.println("Registrar server ready");
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void observeRegistrar() {
        registrar.getCateringFacilityMap().addListener((MapChangeListener<String, CateringFacility>) change -> selectCF.getItems().add(change.getKey()));
        registrar.getVisitorMap().addListener((MapChangeListener<String, Visitor>) change -> selectVis.getItems().add(change.getKey()));
    }


    public void observeCFKey(CateringFacility cateringFacility) {
        if (currentSecretObservableMap != null) {
            currentSecretObservableMap.removeListener(changeListenerSecret);
        }
        currentSecretObservableMap = cateringFacility.getSecretKeyMap();

        dataSecret = FXCollections.observableArrayList(cateringFacility.getSecretKeyMap().entrySet());
        tableSecret.setItems(dataSecret);
        colSecretDate.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<LocalDate, SecretKey>, String> p ) -> new SimpleStringProperty(p.getValue().getKey().format(DateTimeFormatter.ISO_DATE)));
        colSecretKey.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<LocalDate, SecretKey>, String> p ) -> new SimpleStringProperty(Arrays.toString(p.getValue().getValue().getEncoded())));


        changeListenerSecret = change -> {
            dataSecret.clear();
            dataSecret.addAll(cateringFacility.getSecretKeyMap().entrySet());
        };
        currentSecretObservableMap.addListener(changeListenerSecret);
    }

    public void observeCFPseud(CateringFacility cateringFacility) {
        if (currentPseudonymObservableMap != null) {
            currentPseudonymObservableMap.removeListener(changeListenerPseudonym);
        }
        currentPseudonymObservableMap = cateringFacility.getPseudonymMap();

        dataPseudonym = FXCollections.observableArrayList(cateringFacility.getPseudonymMap().entrySet());
        tablePseudonym.setItems(dataPseudonym);
        colPseudonymDate.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<LocalDate, byte[]>, String> p ) -> new SimpleStringProperty(p.getValue().getKey().format(DateTimeFormatter.ISO_DATE)));
        colPseudonymPseudonym.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<LocalDate, byte[]>, String> p ) -> new SimpleStringProperty(Arrays.toString(p.getValue().getValue())));

        changeListenerPseudonym = change -> {
            dataPseudonym.clear();
            dataPseudonym.addAll(cateringFacility.getPseudonymMap().entrySet());
        };
        currentPseudonymObservableMap.addListener(changeListenerPseudonym);
    }

    public void observeComboBoxCF() {
        selectCF.getSelectionModel().selectedItemProperty().addListener((observable) -> {
            CateringFacility cateringFacility = registrar.getCateringFacilityMap().get(String.valueOf(selectCF.getSelectionModel().getSelectedItem()));
            labelCF.setText("Unique info: " + cateringFacility.getCF());
            labelLocation.setText("Location: " + cateringFacility.getLocation());

            observeCFKey(cateringFacility);
            observeCFPseud(cateringFacility);
        });
    }


    public void observeComboBoxVis() {
        selectVis.getSelectionModel().selectedItemProperty().addListener((observable -> {
            Visitor visitor = registrar.getVisitorMap().get(String.valueOf(selectVis.getSelectionModel().getSelectedItem()));
            labelPhone.setText("Phonenumber: " + visitor.getPhoneNumber());

            observeComboBoxDate(visitor);
        }));
    }

    public void observeComboBoxDate(Visitor visitor) {
        if (changeListenerDate != null) {
            currentDateObservableMap.removeListener(changeListenerDate);
        }
        currentDateObservableMap = visitor.getTokensMap();

        selectVisDate.getItems().clear();
        Set<LocalDate> keySet = visitor.getTokensMap().keySet();
        List<String> keyList = new ArrayList<>();
        keySet.forEach(localDate -> keyList.add(localDate.format(DateTimeFormatter.ISO_DATE)));
        selectVisDate.getItems().addAll(keyList);

        changeListenerDate = change -> selectVisDate.getItems().add(change.getKey().format(DateTimeFormatter.ISO_DATE));
        currentDateObservableMap.addListener(changeListenerDate);

        selectVisDate.getSelectionModel().selectedItemProperty().addListener((observable -> {
            listTokens.getItems().clear();
            listSign.getItems().clear();
            listTokens.getItems().addAll(visitor.getTokensMap().get(LocalDate.parse(String.valueOf(selectVisDate.getSelectionModel().getSelectedItem()), DateTimeFormatter.ISO_DATE)));
            List<String> signBase64 = visitor.getSignatureMap().get(LocalDate.parse(String.valueOf(selectVisDate.getSelectionModel().getSelectedItem()), DateTimeFormatter.ISO_DATE));
            List<String> decodedSign = new ArrayList<>();
            signBase64.forEach(s -> decodedSign.add(Arrays.toString(Base64.getDecoder().decode(s))));
            listSign.getItems().addAll(decodedSign);
        }));
    }

}