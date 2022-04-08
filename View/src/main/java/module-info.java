module com.example.view {
    requires javafx.controls;
    requires javafx.fxml;
    opens com.example.view to javafx.fxml;
    exports com.example.view;
    requires com.example.model;
    requires java.desktop;

}