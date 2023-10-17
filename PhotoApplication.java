package com.niceferrari.photoapp;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class PhotoApplication extends Application {

    public Stage photoStage;
    public PhotoController photoController;
    public FileOperation fileOperation;

    @FXML
    private Button eyedropperButton = new Button();
    @FXML
    private Button resizeButton;

    @Override
    public void start(Stage stage) throws IOException {
        photoController = new PhotoController();
        fileOperation = new FileOperation();

        photoStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(PhotoApplication.class.getResource("PhotoApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        photoStage.setOnCloseRequest(e -> {
            e.consume();
            PhotoController.exitApplication(photoStage);
        });

        stage.setScene(scene);
        stage.setTitle("PhotoApp");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}