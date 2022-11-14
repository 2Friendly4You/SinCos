module com.example.sincos {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sincos to javafx.fxml;
    exports com.example.sincos;
}