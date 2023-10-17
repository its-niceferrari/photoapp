package com.niceferrari.photoapp;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileOperation {

    public FileOperation() {

    }

    public void openImage(Canvas canvas, Stage primaryStage) {
        // Displays a dialog and creates a File object for the image to be opened.
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Assigns filepath for the selected image.
        String imagePath = selectedFile.toURI().toString();
        Image image = new Image(imagePath);

        // Clears and resizes the Canvas to the new dimensions.
        canvas.setHeight(image.getHeight());
        canvas.setWidth(image.getWidth());
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draws the image.
        gc.drawImage(image, 0, 0);
    }

    public void saveImage(Canvas canvas, String imagePath) {
        File file;
        try {
            // Saves the Canvas to a BufferedImage that can be used to overwrite the existing file.
            if (imagePath.startsWith("file:")){
                file = new File(imagePath.substring(5));
            } else {
                file = new File(imagePath);
            }
            Image snapshot = canvas.snapshot(null, null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
        }
    }

    /**
     * Opens a file chooser dialog for the user to specify the name and location of the Canvas to be saved.
     * This method allows the user to choose the image name and location, and then saves the Canvas as an image. It
     * will also handle a confirmation alert if the chosen file format may result in a loss of quality.
     *
     * @param canvas The Canvas to be saved as an image.
     * @param stage The main stage of the application.
     *
     * @see Canvas
     * @see Stage
     * @see FileChooser
     * @see FileChooser.ExtensionFilter
     * @see File
     * @see Image
     * @see SwingFXUtils
     * @see BufferedImage
     * @see Alert
     * @see ButtonType
     */
    public void saveAsImage(Canvas canvas, Stage stage) {
        // Allows the user to specify the image name and location before saving.
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files (*.*)", "*.*"));
        File file = fileChooser.showSaveDialog(stage);
        String imagePath = file.toURI().toString();

        // Saves the Canvas to a BufferedImage that can be used to write the file.
        Image snapshot = canvas.snapshot(null, null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);

        String extension = imagePath.substring(imagePath.lastIndexOf(".") + 1);
        if ("jpg".equalsIgnoreCase(extension) || "jpeg".equalsIgnoreCase(extension)) {
            ButtonType cancelButton = new ButtonType("Cancel");
            ButtonType saveButton = new ButtonType("Save");

            Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Saving in this file format will result in a loss of quality. Do you want to continue?",
                    cancelButton, saveButton);
            closeAlert.setTitle("Loss of Quality");
            ButtonType bt = closeAlert.showAndWait().get();
            if (bt == cancelButton) closeAlert.close();
            else if (bt == saveButton) saveImage(canvas, imagePath);
        } else {
            // Save the image in the selected format
            saveImage(canvas, imagePath);
        }
    }

    public void closeImage(TabPane tabPane) {
        ButtonType cancelButton = new ButtonType("Cancel");
        ButtonType exitButton = new ButtonType("Exit Without Saving");

        Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "There are unsaved changes to the image. Do you want to close without saving?",
                cancelButton, exitButton);
        closeAlert.setTitle("Unsaved Changes");

        ButtonType bt = closeAlert.showAndWait().get();
        if (bt == exitButton) tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedItem());
    }
}