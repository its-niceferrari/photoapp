package com.niceferrari.photoapp;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

public class ShapeDraw {
    public ShapeDraw() {

    }

    public void drawSquare(Double startX, Double startY, Double endX, Double endY, GraphicsContext gc) {
        // Determines the longest edge
        double diffX = Math.abs(endX - startX);
        double diffY = Math.abs(endY - startY);
        double highest = Math.max(diffX, diffY);

        gc.fillRect(startX, startY, highest, highest);
    }

    public void drawRectangle(Double startX, Double startY, Double endX, Double endY, GraphicsContext gc) {
        gc.fillRect(startX, startY, endX - startX, endY - startY);
    }

    public void drawCircle(Double startX, Double startY, Double endX, Double endY, GraphicsContext gc) {
        double diffX = Math.abs(endX - startX);
        double diffY = Math.abs(endY - startY);
        double highest = Math.max(diffX, diffY);
        gc.fillOval(startX, startY, highest, highest);
    }

    public void drawEllipse(Double startX, Double startY, Double endX, Double endY, GraphicsContext gc) {
        gc.fillOval(startX, startY, endX - startX, endY - startY);
    }

    public void drawTriangle(Double startX, Double startY, Double endX, Double endY, GraphicsContext gc) {
        // Calculate base and height of the triangle
        double base = Math.abs(endX - startX);
        double height = Math.abs(endY - startY);

        // Calculate vertices of the triangle
        double x1 = startX;
        double y1 = startY;

        double x2 = startX + base;
        double y2 = y1;

        double x3 = startX + base / 2;
        double y3 = startY - height;

        // Draw the filled triangle
        gc.fillPolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
    }

    public void drawPolygon(Double startX, Double startY, Double endX, Double endY, Integer sides, GraphicsContext gc) {
        int centerX = (int) ((startX + endX) / 2);
        int centerY = (int) ((startY + endY) / 2);
        int radius = (int) Math.abs(endX - startX) / 2;

        double[] xPoints = new double[sides];
        double[] yPoints = new double[sides];

        for (int i = 0; i < sides; i++) {
            double angle = 2.0 * Math.PI * i / sides;
            xPoints[i] = centerX + radius * Math.cos(angle);
            yPoints[i] = centerY + radius * Math.sin(angle);
        }

        // Draw the filled polygon
        gc.fillPolygon(xPoints, yPoints, sides);
    }

    public void drawSemiCircle(Double startX, Double startY, Double endX, Double endY, GraphicsContext gc) {
        // Draw the filled half-circle
        gc.fillArc(startX, startY, endX - startX, endY - startY, 0, 180, ArcType.ROUND);
    }
}