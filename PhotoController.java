package com.niceferrari.photoapp;

import java.awt.*;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;

import javafx.fxml.FXML;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.Random;

public class PhotoController {
    @FXML
    public ColorPicker colorPicker;
    @FXML
    public TextField widthField;
    @FXML
    public TextField heightField;
    @FXML
    public Label statusLabel;
    @FXML
    public Button eyedropperButton;
    @FXML
    public Canvas canvas;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public ChoiceBox drawChoiceBox;
    @FXML
    public ChoiceBox penSizeChoiceBox;
    @FXML
    public ChoiceBox shapeChoiceBox;

    public Stage primaryStage = new Stage();
    public GraphicsContext gc;
    public String imagePath;
    public boolean colorPicked = true;
    public double startX, startY, endX, endY;

    public void initialize() {
        // Assigns the GraphicsContext associated with this Canvas.
        gc = canvas.getGraphicsContext2D();

        // Sets default pen settings.
        gc.setStroke(Color.BLACK);
        colorPicker.setValue(Color.BLACK);
        penSizeChoiceBox.setValue("2");
        gc.setLineWidth(2.0);

        // Sets default values for ChoiceBox.
        drawChoiceBox.setValue("Pen");
        shapeChoiceBox.setValue("Square");

        // Sets the text fields to the current dimensions.
        widthField.setText(String.valueOf(canvas.getWidth()));
        heightField.setText(String.valueOf(canvas.getHeight()));

        // Methods to handle drawing with the mouse.
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseReleased(this::onMouseReleased);

        // PhotoApplication.photoStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::exitApplication);
    }

    public void onMousePressed(MouseEvent event) {
        // Gets start coordinates.
        startX = event.getX();
        startY = event.getY();
    }

    public void onMouseDragged(MouseEvent event) {
        // Gets end coordinates.
        endX = event.getX();
        endY = event.getY();

        if (drawChoiceBox.getValue().equals("Pen")) {
            // Draws a line from point to point.
            gc.strokeLine(startX, startY, endX, endY);

            // Updates coordinates.
            startX = event.getX();
            startY = event.getY();
        }
    }

    public void onMouseReleased(MouseEvent event) {
        if (drawChoiceBox.getValue().equals("Line")) {
            // Gets the second pair of coordinates and draws a straight line between the two.
            endX = event.getX();
            endY = event.getY();
            drawLine(gc);
        } else if (drawChoiceBox.getValue().equals("Shape")) {
            // Gets the second pair of coordinates.
            gc.setFill(colorPicker.getValue());
            endX = event.getX();
            endY = event.getY();

            if (shapeChoiceBox.getValue().equals("Square")) {
                // Determines the longest edge
                double diffX = Math.abs(endX - startX);
                double diffY = Math.abs(endY - startY);
                double highest = Math.max(diffX, diffY);
                gc.fillRect(startX, startY, highest, highest);
            } else if (shapeChoiceBox.getValue().equals("Rectangle")) {
                gc.fillRect(startX, startY, endX - startX, endY - startY);
            } else if (shapeChoiceBox.getValue().equals("Circle")) {
                double diffX = Math.abs(endX - startX);
                double diffY = Math.abs(endY - startY);
                double highest = Math.max(diffX, diffY);
                gc.fillOval(startX, startY, highest, highest);
            } else if (shapeChoiceBox.getValue().equals("Ellipse")) {
                gc.fillOval(startX, startY, endX - startX, endY - startY);
            } else if (shapeChoiceBox.getValue().equals("Triangle")) {
                // Define the vertices of the triangle based on mouse positions
                double[] triangleX = {startX, endX, (startX + endX) / 2};
                double[] triangleY = {startY, endY, startY};
                gc.fillPolygon(triangleX, triangleY, 3);
            }
        }
    }

    public void drawLine(GraphicsContext gc) {
        gc.strokeLine(startX, startY, endX, endY);
    }

    public void handlePenSizeChange() {
        String selectedPenSize = (String) penSizeChoiceBox.getValue();
        double penSize = Double.parseDouble(selectedPenSize);

        // Update the pen size on the GraphicsContext
        gc.setLineWidth(penSize);
    }

    public void handleColorChange() {
        Color selectedColor = colorPicker.getValue();
        gc.setStroke(selectedColor);
    }

    public void resizeButtonClick() {
        // Get the text from the widthField and heightField TextFields
        String widthText = widthField.getText();
        String heightText = heightField.getText();

        try {
            // Parse the text into double values
            double width = Double.parseDouble(widthText);
            double height = Double.parseDouble(heightText);

            // Set the canvas dimensions
            canvas.setWidth(width);
            canvas.setHeight(height);
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid input.");
        }
    }

    public void eyedropperButtonClick() {
        colorPicked = false;

        // Add a mouse click event handler to capture the color
        canvas.setOnMouseClicked(event -> {
            if (!colorPicked) {
                Color pickedColor = getColorAt(event.getSceneX(), event.getSceneY());
                gc.setStroke(pickedColor);
                colorPicker.setValue(pickedColor);
                colorPicked = true;
            }
        });
    }

    private Color getColorAt(double x, double y) {
        // Capture the color at the given coordinates
        PixelReader pixelReader = canvas.snapshot(null, null).getPixelReader();
        return pixelReader.getColor((int) x, (int) y);
    }

    public static void exitApplication(Stage photoStage){
        ButtonType cancelButton = new ButtonType("Cancel");
        ButtonType exitButton = new ButtonType("Exit Without Saving");
        ButtonType saveButton = new ButtonType("Save and Exit");

        Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "There are unsaved changes to the image. Do you want to close without saving?",
                cancelButton, exitButton, saveButton);
        closeAlert.setTitle("Unsaved Changes");

        photoStage.setOnCloseRequest(e -> {
            e.consume();
            ButtonType bt = closeAlert.showAndWait().get();
            if (bt == cancelButton) {
                e.consume();
            } else if (bt == exitButton) {
                photoStage.close();
                System.exit(0);
            } else if (bt == saveButton) {
                // saveButtonClick();
                photoStage.close();
                System.exit(0);
            }

        });
    }


    public void openButtonClick() {
        // Displays a dialog and creates a File object for the image to be opened.
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

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

        statusLabel.setText(imagePath);
    }

    public void closeButtonClick() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        statusLabel.setText("No file currently open.");
    }

    public void saveButtonClick() {
        try {
            // Saves the Canvas to a BufferedImage that can be used to overwrite the existing file.
            File file = new File(imagePath.substring(5));
            Image snapshot = canvas.snapshot(null, null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            statusLabel.setText("Error encountered when saving image.");
        }
    }

    public void saveAsButtonClick() {
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
                statusLabel.setText("Error encountered when saving image.");
            }
        }
    }

    public void quitButtonClick() { exitApplication(primaryStage); }

    public void helpButtonClick() {
        // Opens the help text file.
        String aboutPath = "help.txt";
        try {
            File file = new File(aboutPath);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (IOException e) {
            statusLabel.setText("Error encountered when opening the help file.");
        }
    }

    public void aboutButtonClick() {
        // Opens the release notes.
        String aboutPath = "release_notes.txt";
        try {
            File file = new File(aboutPath);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (IOException e) {
            statusLabel.setText("Error encountered when opening the release notes.");
        }
    }

    public void laughButtonClick() {
        Random random = new Random();
        int randomNumber = random.nextInt(3) + 1;

        if (randomNumber == 1) statusLabel.setText("What's the problem with Java jokes? They have no class!");
        else if (randomNumber == 2) statusLabel.setText("I'm trying to teach my cat Java programming...But he" +
                "keeps complaining about a NullLaserPointerException.");
        else statusLabel.setText("Why do Java developers wear glasses? Because they can't C sharp!");
        }
}