package com.niceferrari.photoapp;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;

public class PhotoController {
    @FXML
    private Stage primaryStage;
    @FXML
    private ImageView imageView;
    @FXML
    private String imagePath;

    public void openButtonClick(ActionEvent actionEvent) {
        // Displays a dialog and creates a File object for the image to be opened
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        Image image = new Image(selectedFile.toURI().toString());

        // Handles encoded file path
        String temp = selectedFile.toURI().toString().substring(5);
        imagePath = temp.replaceAll("%20", " ");

        imageView.setImage(image);
    }

    public void closeButtonClick(ActionEvent actionEvent) {
        imageView.setImage(null);
    }

    public void saveButtonClick(ActionEvent actionEvent) throws IOException {
        // Reads the current image open in the window
        File inputFile = new File(imagePath);
        BufferedImage originalImage = ImageIO.read(inputFile);

        // Creates a File object for the image to be saved
        File outputFile = new File(imagePath);

        // Overwrites the image
        ImageIO.write(originalImage, "jpg", outputFile);
    }

    public void saveAsButtonClick(ActionEvent actionEvent) throws IOException {
        // Reads the current image open in the window
        File inputFile = new File(imagePath);
        BufferedImage originalImage = ImageIO.read(inputFile);

        // Displays a dialog and creates a File object for the image to be saved
        FileChooser fileChooser = new FileChooser();
        File outputFile = fileChooser.showSaveDialog(primaryStage);

        // Writes the image to the user-specified location
        ImageIO.write(originalImage, "jpg", outputFile);
    }

    public void quitButtonClick(ActionEvent actionEvent) {
        System.exit(0);
    }
}