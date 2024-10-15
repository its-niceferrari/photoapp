package com.niceferrari.photoapp;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * Controller for the PhotoApp application. Handles the events for the buttons, mouse actions, and initialization.
 *
 * @see Canvas
 * @see GraphicsContext
 * @see Timeline
 * @see File
 * @see MouseEvent
 * @see Alert
 */
public class PhotoController {

    public CanvasSnapshot canvasSnapshot;
    public FileOperation fileOperation;
    public TabOperation tabOperation;
    public PaintStack paintStack;
    public PenDraw penDraw;
    public ShapeDraw shapeDraw;
    public TextFileWriter textFileWriter;
    public AppOperation appOperation;

    public ColorPicker colorPicker;
    public TextField widthField;
    public TextField heightField;
    public Button eyedropperButton;
    public Canvas canvas;
    public ChoiceBox drawChoiceBox;
    public ChoiceBox penSizeChoiceBox;
    public ChoiceBox shapeChoiceBox;
    public Stage primaryStage = new Stage();
    public GraphicsContext gc;
    public String directoryPath = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "PhotoApp" + File.separator + "autosave";
    public String imagePath;
    public boolean colorPicked = true;
    public Color backgroundColor = Color.rgb(244, 244, 244);;
    public double startX, startY, endX, endY;
    public TabPane tabPane;
    public TextField textInputField;
    public ChoiceBox sidesChoiceBox;

    public WritableImage clipboard;
    public WritableImage tempClipboard;
    public Label timerCountdown;

    private static final int AUTO_SAVE_INTERVAL_SECONDS = 300;
    private int countdownSeconds = AUTO_SAVE_INTERVAL_SECONDS;
    private int tempSaveCount = 0;
    private int rotationDegrees = 0;


    /**
     * Initializes the drawing application. Assigns GraphicsContext, creates objects, and sets default values.
     */
    public void initialize() {
        // Assigns the GraphicsContext associated with this Canvas.
        gc = canvas.getGraphicsContext2D();

        canvasSnapshot = new CanvasSnapshot(canvas);
        fileOperation = new FileOperation();
        tabOperation = new TabOperation();
        paintStack = new PaintStack();
        penDraw = new PenDraw();
        shapeDraw = new ShapeDraw();
        textFileWriter = new TextFileWriter();
        appOperation = new AppOperation();

        // Creates a timeline that triggers the autosave every 5 minutes.
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                countdownSeconds--;

                if (countdownSeconds == 60) {
                    Notifications.create()
                            .title("Auto Save Reminder")
                            .text("The image will be auto-saved in 1 minute.")
                            .showInformation();
                }

                if (countdownSeconds <= 0) {
                    // Resets the countdown and perform autosave.
                    countdownSeconds = AUTO_SAVE_INTERVAL_SECONDS;

                    // Creates the directory if it doesn't exist.
                    File directory = new File(directoryPath);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }

                    imagePath = directoryPath + File.separator + tempSaveCount + ".png";

                    // Saves the image and increments the save count.
                    fileOperation.saveImage(canvas, imagePath);
                    tempSaveCount++;
                }

                // Updates the label with the current countdown value.
                timerCountdown.setText(String.valueOf(countdownSeconds));
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Adds a listener to the selection model of the TabPane.
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldTab, newTab) -> tabSelectionChanged(newTab)
        );

        // Methods to handle drawing with the mouse.
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseReleased(this::onMouseReleased);

        // Sets default pen settings.
        gc.setStroke(Color.BLACK);
        colorPicker.setValue(Color.BLACK);
        penSizeChoiceBox.setValue("2");
        gc.setLineWidth(2.0);
        gc.setFont(Font.font("Arial", 20));
        gc.setFill(Color.BLACK);

        // Sets default values for ChoiceBox.
        drawChoiceBox.setValue("Pen");
        shapeChoiceBox.setValue("Square");

        // Sets the text fields to the current dimensions.
        widthField.setText(String.valueOf(canvas.getWidth()));
        heightField.setText(String.valueOf(canvas.getHeight()));
    }

    private void tabSelectionChanged(Tab selectedTab) {
        gc = tabOperation.tabSelectionChanged(selectedTab, gc);
        textFileWriter.writeMessageToFile("Tab selection changed to " + selectedTab.getText());
    }

    /**
     * Handles the mouse press event based on the selected drawing choice.
     *
     * @param event The MouseEvent representing the mouse press event.
     */
    public void onMousePressed(MouseEvent event) {
        // Gets start coordinates.
        startX = event.getX();
        startY = event.getY();

        if (drawChoiceBox.getValue().equals("Text")) {
            // Draws text on the Canvas.
            String textInput = textInputField.getText();
            gc.fillText(textInput, startX, startY);
            textFileWriter.writeMessageToFile("Text drawn on the Canvas.");
        }
    }

    /**
     * Handles the mouse drag event based on the selected drawing choice.
     *
     * @param event The MouseEvent representing the mouse drag event.
     */
    public void onMouseDragged(MouseEvent event) {
        // Gets end coordinates.
        endX = event.getX();
        endY = event.getY();

        if (drawChoiceBox.getValue().equals("Pen")) {
            // Draws a line from point to point.
            gc.setStroke(colorPicker.getValue());
            gc.strokeLine(startX, startY, endX, endY);

            // Updates coordinates.
            startX = event.getX();
            startY = event.getY();
        } else if (drawChoiceBox.getValue().equals("Eraser")) {
            // Draws a white line from point to point.
            gc.setStroke(backgroundColor);
            gc.strokeLine(startX, startY, endX, endY);

            startX = event.getX();
            startY = event.getY();
        }
    }

    /**
     * Handles the mouse release event based on the selected drawing choice.

     * @param event The MouseEvent representing the mouse release event.
     */
    public void onMouseReleased(MouseEvent event) {
        if (drawChoiceBox.getValue().equals("Select")) {
            // Copies the selected area to the clipboard.
            double width = Math.abs(endX - startX);
            double height = Math.abs(endY - startY);

            tempClipboard = new WritableImage((int) width, (int) height);
            canvas.snapshot(null, clipboard);
            textFileWriter.writeMessageToFile("Selected area copied to clipboard.");
        } else if (drawChoiceBox.getValue().equals("Solid Line")) {
            // Draws a straight line between two points.
            endX = event.getX();
            endY = event.getY();
            penDraw.drawLine(startX, startY, endX, endY, gc);
            textFileWriter.writeMessageToFile("Line drawn on the Canvas.");

        } else if (drawChoiceBox.getValue().equals("Dashed Line")) {
            // Draws a straight line between two points.
            endX = event.getX();
            endY = event.getY();
            penDraw.drawDashedLine(startX, startY, endX, endY, gc);
            textFileWriter.writeMessageToFile("Line drawn on the Canvas.");

        } else if (drawChoiceBox.getValue().equals("Shape")) {
            // Gets the second pair of coordinates.
            gc.setFill(colorPicker.getValue());
            endX = event.getX();
            endY = event.getY();

            if (shapeChoiceBox.getValue().equals("Square")) {
                shapeDraw.drawSquare(startX, startY, endX, endY, gc);

            } else if (shapeChoiceBox.getValue().equals("Rectangle")) {
                shapeDraw.drawRectangle(startX, startY, endX, endY, gc);

            } else if (shapeChoiceBox.getValue().equals("Circle")) {
                shapeDraw.drawCircle(startX, startY, endX, endY, gc);

            } else if (shapeChoiceBox.getValue().equals("Ellipse")) {
                shapeDraw.drawEllipse(startX, startY, endX, endY, gc);

            } else if (shapeChoiceBox.getValue().equals("Triangle")) {
                shapeDraw.drawTriangle(startX, startY, endX, endY, gc);

            } else if (shapeChoiceBox.getValue().equals("Polygon")) {
                int sides = Integer.parseInt((String) sidesChoiceBox.getValue());
                shapeDraw.drawPolygon(startX, startY, endX, endY, sides, gc);

            } else if (shapeChoiceBox.getValue().equals("Semi-Circle")) {
                shapeDraw.drawSemiCircle(startX, startY, endX, endY, gc);
            }
            textFileWriter.writeMessageToFile("Shape drawn on the Canvas.");
        }
        else if (drawChoiceBox.getValue().equals("Stamp")){
            endX = event.getX();
            endY = event.getY();
            penDraw.drawStamp(startX, startY, endX, endY, gc);
            textFileWriter.writeMessageToFile("Stamp drawn on the Canvas.");
        }
        canvasSnapshot = new CanvasSnapshot(canvas);
        paintStack.pushSnapshot(canvasSnapshot);
    }

    public void handlePenSizeChange() {
        String selectedPenSize = (String) penSizeChoiceBox.getValue();
        penDraw.handlePenSizeChange(selectedPenSize, gc);
        textFileWriter.writeMessageToFile("Pen size changed to " + selectedPenSize + ".");
    }

    public void handleColorChange() {
        Color selectedColor = colorPicker.getValue();
        gc.setStroke(selectedColor);
        textFileWriter.writeMessageToFile("Pen color changed to " + selectedColor.toString() + ".");
    }

    /**
     * Handles the button click event for resizing the canvas. Sets the canvas dimensions to the values in the text.
     */
    public void resizeButtonClick() {
        // Gets the text from the widthField and heightField.
        String widthText = widthField.getText();
        String heightText = heightField.getText();

        try {
            // Parses the text into double values.
            double width = Double.parseDouble(widthText);
            double height = Double.parseDouble(heightText);

            // Sets the canvas dimensions.
            canvas.setWidth(width);
            canvas.setHeight(height);
            textFileWriter.writeMessageToFile("Canvas resized to " + width + " x " + height + ".");
        } catch (NumberFormatException e) {
            // Displays an error message if the text cannot be parsed.
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Invalid dimensions.");
            errorAlert.showAndWait();
        }
    }

    /**
     * Handles the button click event for rotating the canvas. Rotates the canvas by 90 degrees.
     */
    public void rotateButtonClick() {
        gc.rotate(90);
        rotationDegrees += 90;
        if (rotationDegrees == 360) rotationDegrees = 0;
        gc.getCanvas().setRotate(rotationDegrees);
        paintStack.pushSnapshot(canvasSnapshot);
        textFileWriter.writeMessageToFile("Canvas rotated by 90 degrees.");
    }

    /**
     * Handles the button click event for flipping the canvas. Flips the canvas horizontally.
     */
    public void horizontalButtonClick() {
        // Flips canvas horizontally.
        gc.scale(-1, 1);
        gc.drawImage(gc.getCanvas().snapshot(null, null), -(canvas.getWidth()), 0);
        paintStack.pushSnapshot(canvasSnapshot);
        textFileWriter.writeMessageToFile("Canvas flipped horizontally.");
    }

    /**
     * Handles the button click event for flipping the canvas. Flips the canvas vertically.
     */
    public void verticalButtonClick() {
        // Flips canvas vertically.
        rotateButtonClick();
        rotateButtonClick();
        horizontalButtonClick();
        paintStack.pushSnapshot(canvasSnapshot);
        textFileWriter.writeMessageToFile("Canvas flipped vertically.");
    }

    /**
     * Handles the button click event for picking a color from the canvas. Sets the pen color to the color picked.
     */
    public void eyedropperButtonClick() {
        colorPicked = false;

        // Adds an event handler to capture the color picked.
        canvas.setOnMouseClicked(event -> {
            if (!colorPicked) {
                Color pickedColor = getColorAt(event.getSceneX(), event.getSceneY());
                gc.setStroke(pickedColor);
                colorPicker.setValue(pickedColor);
                colorPicked = true;
                textFileWriter.writeMessageToFile("Color picked from the Canvas.");
            }
        });
    }

    private Color getColorAt(double x, double y) {
        // Captures the color at the given coordinates.
        PixelReader pixelReader = canvas.snapshot(null, null).getPixelReader();
        return pixelReader.getColor((int) x, (int) y);
    }

    /**
     * Handles the button click event for creating a new tab. Creates a new tab and assigns the GraphicsContext.
     */
    public void newButtonClick() {
        Tab newTab = new Tab("New Tab");
        Canvas canvas = new Canvas(1280, 720);
        newTab.setContent(canvas);

        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);

        // Assign the GraphicsContext associated with this Canvas.
        gc = canvas.getGraphicsContext2D();

        canvasSnapshot = new CanvasSnapshot(canvas);
        paintStack = new PaintStack();

        // Sets default pen settings.
        gc.setStroke(Color.BLACK);
        colorPicker.setValue(Color.BLACK);
        penSizeChoiceBox.setValue("2");
        gc.setLineWidth(2.0);
        gc.setFont(Font.font("Arial", 20));
        gc.setFill(Color.BLACK);

        // Methods to handle drawing with the mouse.
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseReleased(this::onMouseReleased);

        textFileWriter.writeMessageToFile("New tab created.");
    }


    public void openButtonClick() {
        fileOperation.openImage(canvas, primaryStage);
        textFileWriter.writeMessageToFile("Image opened.");
    }

    public void closeButtonClick() {
        fileOperation.closeImage(tabPane);
        textFileWriter.writeMessageToFile("Image closed.");
    }

    public void saveButtonClick() {
        if (imagePath != null) {
            fileOperation.saveImage(canvas, imagePath);
            textFileWriter.writeMessageToFile("Image saved.");
        }
        else {
            fileOperation.saveAsImage(canvas, primaryStage);
            textFileWriter.writeMessageToFile("Image saved.");
        }
    }

    public void saveAsButtonClick() {
        fileOperation.saveAsImage(canvas, primaryStage);
        textFileWriter.writeMessageToFile("Image saved.");
    }

    public void quitButtonClick() {
        appOperation.quitButtonClick(primaryStage);
    }

    public void helpButtonClick() {
        appOperation.helpButtonClick();
    }

    public void aboutButtonClick() {
        appOperation.aboutButtonClick();
    }

    public void laughButtonClick() {
        appOperation.laughButtonClick();
    }

    public void undoButtonClick(ActionEvent actionEvent) {
        if (paintStack.canUndo()) {
            canvasSnapshot = paintStack.popUndo();
            canvasSnapshot.restoreSnapshot(canvas);
            textFileWriter.writeMessageToFile("Undo performed.");
        }
    }

    public void redoButtonClick(ActionEvent actionEvent) {
        if (paintStack.canRedo()) {
            canvasSnapshot = paintStack.popRedo();
            canvasSnapshot.restoreSnapshot(canvas);
            textFileWriter.writeMessageToFile("Redo performed.");
        }
    }

    /**
     * Handles the button click event for selecting an area. Copies the selected area to the clipboard.
     */
    public void copyButtonClick(ActionEvent actionEvent) {
        clipboard = new WritableImage(tempClipboard.getPixelReader(), (int) tempClipboard.getWidth(), (int) tempClipboard.getHeight());
        textFileWriter.writeMessageToFile("Selected area copied to clipboard.");
    }

    /**
     * Handles the button click event for pasting an area. Pastes the selected area from the clipboard.
     */
    public void pasteButtonClick(ActionEvent actionEvent) {
        if (clipboard != null) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.drawImage(clipboard, 0,0);
            textFileWriter.writeMessageToFile("Selected area pasted from clipboard.");
        }
    }

    /**
     * Handles the button click event for clearing the canvas.
     */
    public void clearButtonClick(ActionEvent actionEvent) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        textFileWriter.writeMessageToFile("Canvas cleared.");
    }
}