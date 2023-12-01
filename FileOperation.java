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

/**
 * Handles all file operations for the application including opening, saving, and closing images.
 *
 * @see Canvas
 * @see GraphicsContext
 * @see FileChooser
 * @see File
 * @see Image
 * @see SwingFXUtils
 * @see BufferedImage
 * @see Alert
 * @see ButtonType
 * @see Stage
 * @see TabPane
 */
public class FileOperation {

    public FileOperation() {

    }

    /**
     * Opens a file chooser dialog for the user to select an image. The image will then be displayed on the Canvas.
     *
     * @param canvas The Canvas to be opened.
     * @param primaryStage The main stage of the application.
     */
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

    /**
     * Saves the Canvas as an image in the same location as the original image.
     *
     * @param canvas The Canvas to be saved as an image.
     * @param imagePath The path of the image to be overwritten.
     */
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
            // Saves the image in the selected format.
            saveImage(canvas, imagePath);
        }
    }

    /**
     * Closes the image in the selected tab. If there are unsaved changes, an alert will be displayed.
     *
     * @param tabPane The TabPane containing the selected tab.
     */
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