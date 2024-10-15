package com.niceferrari.photoapp;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Handles application operations within the File and Help menus.
 *
 * @see Alert
 * @see ButtonType
 * @see Stage
 * @see Desktop
 * @see File
 */
public class AppOperation {

    String helpPath = "help.txt";
    String releaseNotesPath = "release_notes.txt";


    /**
     * Confirms with the user before exiting the application.
     * Displays an alert when attempting to close the application before saving. It allows the user to cancel and
     * consume the event, or exit without saving and close the application.
     *
     * @param photoStage The main stage of the application.
     */
    public void quitButtonClick(Stage photoStage) {
        ButtonType cancelButton = new ButtonType("Cancel");
        ButtonType exitButton = new ButtonType("Exit Without Saving");

        Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "There are unsaved changes to the image. Do you want to close without saving?",
                cancelButton, exitButton);
        closeAlert.setTitle("Unsaved Changes");
        ButtonType bt = closeAlert.showAndWait().get();
        if (bt == cancelButton) {
            closeAlert.close();
        } else if (bt == exitButton) {
            photoStage.close();
            System.exit(0);
        }
    }

    /**
     * Opens the help text file when the help button is clicked.
     * Attempts to open the help text file. Otherwise, an error alert is displayed.
     */
    public void helpButtonClick() {
        try {
            File file = new File(helpPath);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error encountered when opening the help file.");
            errorAlert.showAndWait();
        }
    }

    /**
     * Opens the release notes when the about button is clicked.
     * Attempts to open the release notes file. Otherwise, an error alert is displayed.
     */
    public void aboutButtonClick() {
        try {
            File file = new File(releaseNotesPath);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error encountered when opening the release notes.");
            errorAlert.showAndWait();
        }
    }

    /**
     * Displays a random Java-related joke when the laugh button is clicked.
     * Generates a random number to select a joke to be displayed in an information alert.
     */
    public void laughButtonClick() {
        Random random = new Random();
        int randomNumber = random.nextInt(3) + 1;
        String javaJoke;

        if (randomNumber == 1) javaJoke = "What's the problem with Java jokes? They have no class!";
        else if (randomNumber == 2) javaJoke = "I'm trying to teach my cat Java programming...But he " +
                "keeps complaining about a NullLaserPointerException.";
        else javaJoke = "Why do Java developers wear glasses? Because they can't C sharp!";

        Alert funnyAlert = new Alert(Alert.AlertType.INFORMATION, javaJoke);
        funnyAlert.showAndWait();
    }

    /**
     * Configures the exit behavior for the application when the main stage is closed.
     * Displays a confirmation alert if there are unsaved changes to the image.
     *
     * @param photoStage The main stage of the application.
     */
    public void exitApplication(Stage photoStage){
        ButtonType cancelButton = new ButtonType("Cancel");
        ButtonType exitButton = new ButtonType("Exit Without Saving");

        Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "There are unsaved changes to the image. Do you want to close without saving?",
                cancelButton, exitButton);
        closeAlert.setTitle("Unsaved Changes");

        photoStage.setOnCloseRequest(e -> {
            e.consume();
            ButtonType bt = closeAlert.showAndWait().get();
            if (bt == cancelButton) {
                e.consume();
            } else if (bt == exitButton) {
                photoStage.close();
                System.exit(0);
            }
        });
    }
}