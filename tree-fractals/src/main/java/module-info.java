module com.example.treefractals {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.treefractals to javafx.fxml;
    exports com.example.treefractals;
}