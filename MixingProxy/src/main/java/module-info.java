module com.example.mixingproxy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;


    opens com.example.mixingproxy to javafx.fxml;
    exports com.example.mixingproxy;
}