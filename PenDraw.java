package com.niceferrari.photoapp;

import javafx.scene.canvas.GraphicsContext;

public class PenDraw {
    public PenDraw() {

    }

    public void handlePenSizeChange(String selectedPenSize, GraphicsContext gc) {
        double penSize = Double.parseDouble(selectedPenSize);

        // Update the pen size on the GraphicsContext
        gc.setLineWidth(penSize);
    }

    public void drawLine(Double startX, Double startY, Double endX, Double endY, GraphicsContext gc) {
        gc.strokeLine(startX, startY, endX, endY);
    }
}
