module com.example.doctor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.doctor to javafx.fxml;
    exports com.example.doctor;
}