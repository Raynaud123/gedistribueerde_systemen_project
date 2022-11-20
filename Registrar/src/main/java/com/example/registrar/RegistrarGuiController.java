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


    public ObservableMap<LocalDate, byte[]> currentPseudonymObservableMap;
    public ObservableList<Map.Entry<LocalDate, byte[]>> dataPseudonym;
    public MapChangeListener<LocalDate, byte[]> changeListenerPseudonym;

    public TableView<Map.Entry<LocalDate, byte[]>> tablePseudonym;
    public TableColumn<Map.Entry<LocalDate, byte[]>, String> colPseudonymDate;
    public TableColumn<Map.Entry<LocalDate, byte[]>, String> colPseudonymPseudonym;


    public void initialize() {
        startServer();
        observeRegistrar();
        observeComboBox();
    }

    public void startServer() {
        try {
            Registry registry = LocateRegistry.createRegistry(1112);
            registry.bind("RegistrarService", new RegistrarImpl(this.registrar));
            System.out.println("Registrar server ready");
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