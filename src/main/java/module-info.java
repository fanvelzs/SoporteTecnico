module com.example.soportetecnico {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.soportetecnico to javafx.fxml;
    exports com.example.soportetecnico;
}