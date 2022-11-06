module com.example.visitor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.visitor to javafx.fxml;
    exports com.example.visitor;
}