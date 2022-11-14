package com.example.registrar;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.crypto.SecretKey;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

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

    public ObservableMap<LocalDate, byte[]> currentPseudObservableMap;
    public ObservableList<Map.Entry<LocalDate, byte[]>> dataPseud;
    public MapChangeListener<LocalDate, byte[]> changeListenerPseud;

    public TableView<Map.Entry<LocalDate, byte[]>> tablePseud;
    public TableColumn<Map.Entry<LocalDate, byte[]>, String> colPseudDate;
    public TableColumn<Map.Entry<LocalDate, byte[]>, String> colPseudPseud;


    public void initialize() {
        startServer();
        observeRegistrar();
        observeComboBox();
    }

    public void startServer() {
        try {
            Registry registry = LocateRegistry.createRegistry(1112);
            registry.bind("RegistrarService", new RegistrarImpl(this.registrar));
            System.out.println("server started");
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void observeRegistrar() {
        registrar.getCateringFacilityMap().addListener((MapChangeListener<String , CateringFacility>) change -> selectCF.getItems().add(change.getKey()));
    }

    public void observeCFKey(CateringFacility cateringFacility) {
        if (currentSecretObservableMap != null) {
            currentSecretObservableMap.removeListener(changeListenerSecret);
        }
        currentSecretObservableMap = cateringFacility.getSecretKeyMap();

        dataSecret = FXCollections.observableArrayList(cateringFacility.getSecretKeyMap().entrySet());
        tableSecret.setItems(dataSecret);
        colSecretDate.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<LocalDate, SecretKey>, String> p ) -> new SimpleStringProperty(p.getValue().getKey().format(DateTimeFormatter.ISO_DATE)));
        colSecretKey.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<LocalDate, SecretKey>, String> p ) -> new SimpleStringProperty(p.getValue().getValue().toString()));


        changeListenerSecret = change -> {
            dataSecret.clear();
            dataSecret.addAll(cateringFacility.getSecretKeyMap().entrySet());
        };
        currentSecretObservableMap.addListener(changeListenerSecret);
    }

    public void observeCFPseud(CateringFacility cateringFacility) {
        if (currentPseudObservableMap != null) {
            currentPseudObservableMap.removeListener(changeListenerPseud);
        }
        currentPseudObservableMap = cateringFacility.getPseudonymMap();

        dataPseud = FXCollections.observableArrayList(cateringFacility.getPseudonymMap().entrySet());
        tablePseud.setItems(dataPseud);
        colPseudDate.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<LocalDate, byte[]>, String> p ) -> new SimpleStringProperty(p.getValue().getKey().format(DateTimeFormatter.ISO_DATE)));
        colPseudPseud.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<LocalDate, byte[]>, String> p ) -> new SimpleStringProperty(Arrays.toString(p.getValue().getValue())));

        changeListenerPseud = change -> {
            dataPseud.clear();
            dataPseud.addAll(cateringFacility.getPseudonymMap().entrySet());
        };
        currentPseudObservableMap.addListener(changeListenerPseud);
    }

    public void observeComboBox() {
        selectCF.getSelectionModel().selectedItemProperty().addListener((observableValue) -> {
            CateringFacility cateringFacility = registrar.getCateringFacilityMap().get(String.valueOf(selectCF.getSelectionModel().getSelectedItem()));
            labelCF.setText("Unique info: " + cateringFacility.getCF());
            labelLocation.setText("Location: " + cateringFacility.getLocation());

            observeCFKey(cateringFacility);
            observeCFPseud(cateringFacility);

        });
    }



}