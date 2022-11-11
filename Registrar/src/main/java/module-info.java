module com.example.registrar {
    requires javafx.controls;
    requires javafx.fxml;
    requires hkdf;
    requires java.rmi;


    opens com.example.registrar to javafx.fxml;
    exports com.example.registrar;
}