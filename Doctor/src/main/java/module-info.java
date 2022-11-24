module com.example.doctor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.sql;
    requires json.simple;
    requires com.example.matchingservice;


    opens com.example.doctor to javafx.fxml;
    exports com.example.doctor;
}