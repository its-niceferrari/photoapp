package com.niceferrari.photoapp;

import javafx.scene.canvas.GraphicsContext;

/**
 * This class handles the drawing of lines, stamps, and pen size changes.
 *
 * @see GraphicsContext
 */
public class PenDraw {
    public PenDraw() {

    }

    /**
     * Handles pen size changes.
     *
     * @param selectedPenSize The selected pen size.
     * @param gc              The GraphicsContext of the canvas.
     */
    public void handlePenSizeChange(String selectedPenSize, GraphicsContext gc) {
        double penSize = Double.parseDouble(selectedPenSize);

        // Update the pen size on the GraphicsContext
        gc.setLineWidth(penSize);
    }

    /**
     * Handles drawing lines.
     *
     * @param startX           The starting X coordinate of the line.
     * @param startY           The starting Y coordinate of the line.
     * @param endX             The ending X coordinate of the line.
     * @param endY             The ending Y coordinate of the line.
     * @param gc               The GraphicsContext of the canvas.
     */
    public void drawLine(Double startX, Double startY, Double endX, Double endY, GraphicsContext gc) {
        gc.setLineDashes();
        gc.strokeLine(startX, startY, endX, endY);
    }

    /**
     * Handles dashed line drawing.
     *
     * @param startX           The starting X coordinate of the line.
     * @param startY           The starting Y coordinate of the line.
     * @param endX             The ending X coordinate of the line.
     * @param endY             The ending Y coordinate of the line.
     * @param gc               The GraphicsContext of the canvas.
     */
    public void drawDashedLine(double startX, double startY, double endX, double endY, GraphicsContext gc) {
        double[] dashes = {10, 5, 10, 5};
        gc.setLineDashes(dashes);
        gc.strokeLine(startX, startY, endX, endY);
    }

    /**
     * Handles drawing stamps.
     *
     * @param startX           The starting X coordinate of the line.
     * @param startY           The starting Y coordinate of the line.
     * @param endX             The ending X coordinate of the line.
     * @param endY             The ending Y coordinate of the line.
     * @param gc               The GraphicsContext of the canvas.
     */
    public void drawStamp(double startX, double startY, double endX, double endY, GraphicsContext gc) {
        // Inserts a 40x40 image of the canvas.
        gc.drawImage(gc.getCanvas().snapshot(null, null), startX, startY, 40, 40);
    }
}