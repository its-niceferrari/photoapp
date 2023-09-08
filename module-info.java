module com.niceferrari.photoapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.niceferrari.photoapp to javafx.fxml;
    exports com.niceferrari.photoapp;
}