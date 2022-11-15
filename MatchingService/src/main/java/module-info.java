module com.example.matchingservice {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.sql;


    opens com.example.matchingservice to javafx.fxml;
    exports com.example.matchingservice;
}