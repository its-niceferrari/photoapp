module com.niceferrari.photoapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;

    opens com.niceferrari.photoapp to javafx.fxml;
    exports com.niceferrari.photoapp;
}