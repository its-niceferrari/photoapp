package com.niceferrari.photoapp;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Initializes the application, sets up the main stage, and manages key components.
 *
 * @see FXML
 * @see Stage
 * @see PhotoController
 * @see FileOperation
 * @see AppOperation
 */
public class PhotoApplication extends Application {

    public Stage photoStage;
    public PhotoController photoController;
    public FileOperation fileOperation;
    public AppOperation appOperator;

    @FXML
    private Button eyedropperButton = new Button();
    @FXML
    private Button resizeButton;

    /**
     * Initializes the application, sets up the main stage, and manages key components.
     *
     * @param stage The main stage of the application.
     */
    @Override
    public void start(Stage stage) throws IOException {
        photoController = new PhotoController();
        fileOperation = new FileOperation();
        appOperator = new AppOperation();

        photoStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(PhotoApplication.class.getResource("PhotoApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        photoStage.setOnCloseRequest(e -> {
            e.consume();
            appOperator.exitApplication(photoStage);
        });

        stage.setScene(scene);
        stage.setTitle("PhotoApp");
        stage.show();
    }

    /**
     * Launches the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}