module com.example.gkpaintprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.gkpaintprojekt to javafx.fxml;
    exports com.example.gkpaintprojekt;
}