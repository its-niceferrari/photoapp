package com.niceferrari.photoapp;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class PhotoController {
    @FXML
    public ColorPicker colorPicker;
    @FXML
    private Stage primaryStage;
    @FXML
    private String imagePath;

    private String simplePath;

    private boolean canvasAdded = false;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    @FXML
    private ChoiceBox choiceBox;

    private GraphicsContext gc;
    private double prevX, prevY;

    public void initialize() {
        // Assigns the GraphicsContext associated with this Canvas.
        gc = canvas.getGraphicsContext2D();

        // Sets defaults when starting application.
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2.0);

        // Methods to handle drawing with the mouse.
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);
    }

    public void onMousePressed(MouseEvent event) {
        prevX = event.getX();
        prevY = event.getY();
    }
    public void onMouseDragged(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        // Draws a line from point to point.
        gc.strokeLine(prevX, prevY, x, y);

        // Updates coordinates.
        prevX = x;
        prevY = y;
    }
    public void openButtonClick(ActionEvent actionEvent) {
        // Displays a dialog and creates a File object for the image to be opened.
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        // Adds the Canvas to the AnchorPane if necessary.
        if (!canvasAdded){
            anchorPane.getChildren().add(canvas);
            canvasAdded = true;
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Assigns filepath for the selected image.
        imagePath = selectedFile.toURI().toString();
        Image image = new Image(imagePath);

        // Clears and resizes the Canvas to the new dimensions.
        canvas.setHeight(image.getHeight());
        canvas.setWidth(image.getWidth());
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draws the image.
        gc.drawImage(image,0,0);
    }

    public void closeButtonClick(ActionEvent actionEvent) {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void saveButtonClick(ActionEvent actionEvent) throws IOException {
        try {
            // Saves the Canvas to a BufferedImage that can be used to overwrite the existing file.
            File file = new File(imagePath.substring(5));
            Image snapshot = canvas.snapshot(null, null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAsButtonClick(ActionEvent actionEvent) throws IOException {
        // Allows the user to specify the image name and location before saving.
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                // Saves the Canvas to a BufferedImage that can be used to write the file.
                Image snapshot = canvas.snapshot(null, null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
                ImageIO.write(bufferedImage, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void quitButtonClick(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void helpButtonClick(ActionEvent actionEvent) {
        // Opens the help text file.
        String aboutPath = "help.txt";
        try {
            File file = new File(aboutPath);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void aboutButtonClick(ActionEvent actionEvent) {
        // Opens the release notes.
        String aboutPath = "release_notes.txt";
        try {
            File file = new File(aboutPath);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handlePenSizeChange(ActionEvent actionEvent) {
        String selectedPenSize = (String) choiceBox.getValue();
        double penSize = Double.parseDouble(selectedPenSize);

        // Update the pen size on the GraphicsContext
        gc.setLineWidth(penSize);
    }

    public void handleColorChange(ActionEvent actionEvent) {
        Color selectedColor = colorPicker.getValue();
        gc.setStroke(selectedColor);
    }
}