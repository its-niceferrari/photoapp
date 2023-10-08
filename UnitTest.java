package com.niceferrari.photoapp;

import javafx.application.Application;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.TestClassOrder;

import java.io.File;

public class UnitTest extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PhotoApp.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    // Tests that the directory path is correct.
    @Test
    void testDirectoryPath() {
        PhotoController photoController = new PhotoController();
        String path = photoController.directoryPath;
        assertEquals(System.getProperty("user.home") + File.separator + "Documents" + File.separator +
                "PhotoApp" + File.separator + "autosave", path);
    }

    // Tests that the default pen color is black.
    @Test
    void testDefaultPenColor() {
        PhotoController photoController = new PhotoController();
        String color = photoController.colorPicker.getValue().toString();
        assertEquals("Black", color);
    }

    // Tests that the default pen size is 1.0.
    @Test
    void testDefaultPenSize() {
        PhotoController photoController = new PhotoController();
        double size = (double) photoController.penSizeChoiceBox.getValue();
        assertEquals(1.0, size);
    }
}
