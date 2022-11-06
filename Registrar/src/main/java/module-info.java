module com.example.registrar {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.registrar to javafx.fxml;
    exports com.example.registrar;
}