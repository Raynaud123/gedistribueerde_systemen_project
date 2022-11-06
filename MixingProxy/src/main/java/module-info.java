module com.example.mixingproxy {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mixingproxy to javafx.fxml;
    exports com.example.mixingproxy;
}