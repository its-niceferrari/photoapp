package com.niceferrari.photoapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PhotoApplication extends Application {

    public Stage photoStage;

    @Override
    public void start(Stage stage) throws IOException {
        photoStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(PhotoApplication.class.getResource("PhotoApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("PhotoApp");

        photoStage.setOnCloseRequest(e -> {
            e.consume();
            PhotoController.exitApplication(photoStage);
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}