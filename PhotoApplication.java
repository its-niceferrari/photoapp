package com.niceferrari.photoapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PhotoApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PhotoApplication.class.getResource("PhotoApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("PhotoApp");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}