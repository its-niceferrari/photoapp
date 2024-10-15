package com.niceferrari.photoapp;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

/**
 * Handles the drawing of shapes.
 *
 * @see GraphicsContext
 * @see ArcType
 */
public class ShapeDraw {
    public ShapeDraw() {

    }

    /**
     * Draws a square.
     *
     * @param startX The starting X coordinate of the square.
     * @param startY The starting Y coordinate of the square.
     * @param endX   The ending X coordinate of the square.
     * @param endY   The ending Y coordinate of the square.
     * @param gc     The GraphicsContext of the canvas.
     */
    public void drawSquare(Double startX, Double startY, Double endX, Double endY, GraphicsContext gc) {
        // Determines the longest edge.
        double diffX = Math.abs(endX - startX);
        double diffY = Math.abs(endY - startY);
        double highest = Math.max(diffX, diffY);

        gc.fillRect(startX, startY, highest, highest);
    }

    /**
     * Draws a rectangle.
     *
     * @param startX The starting X coordinate of the rectangle.
     * @param startY The starting Y coordinate of the rectangle.
     * @param endX   The ending X coordinate of the rectangle.
     * @param endY   The ending Y coordinate of the rectangle.
     * @param gc     The GraphicsContext of the canvas.
     */
    public void drawRectangle(Double startX, Double startY, Double endX, Double endY, GraphicsContext gc) {
        gc.fillRect(startX, startY, endX - startX, endY - startY);
    }

    /**
     * Draws a circle.
     *
     * @param startX The starting X coordinate of the circle.
     * @param startY The starting Y coordinate of the circle.
     * @param endX   The ending X coordinate of the circle.
     * @param endY   The ending Y coordinate of the circle.
     * @param gc     The GraphicsContext of the canvas.
     */
    public void drawCircle(Double startX, Double startY, Double endX, Double endY, GraphicsContext gc) {
        double diffX = Math.abs(endX - startX);
        double diffY = Math.abs(endY - startY);
        double highest = Math.max(diffX, diffY);
        gc.fillOval(startX, startY, highest, highest);
    }

    /**
     * Draws an ellipse.
     *
     * @param startX The starting X coordinate of the ellipse.
     * @param startY The starting Y coordinate of the ellipse.
     * @param endX   The ending X coordinate of the ellipse.
     * @param endY   The ending Y coordinate of the ellipse.
     * @param gc     The GraphicsContext of the canvas.
     */
    public void drawEllipse(Double startX, Double startY, Double endX, Double endY, GraphicsContext gc) {
        gc.fillOval(startX, startY, endX - startX, endY - startY);
    }

    /**
     * Draws a triangle.
     *
     * @param startX The starting X coordinate of the triangle.
     * @param startY The starting Y coordinate of the triangle.
     * @param endX   The ending X coordinate of the triangle.
     * @param endY   The ending Y coordinate of the triangle.
     * @param gc     The GraphicsContext of the canvas.
     */
    public void drawTriangle(Double startX, Double startY, Double endX, Double endY, GraphicsContext gc) {
        // Calculates the base and height of the triangle.
        double base = endX - startX;
        double height = endY - startY;

        // Calculates the vertices of the triangle.
        double x1 = startX;
        double y1 = endY;

        double x2 = startX + base;
        double y2 = y1;

        double x3 = startX + base / 2;
        double y3 = endY - height;

        // Draws the filled triangle.
        gc.fillPolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
    }

    /**
     * Draws a regular polygon.
     *
     * @param startX The starting X coordinate of the polygon.
     * @param startY The starting Y coordinate of the polygon.
     * @param endX   The ending X coordinate of the polygon.
     * @param endY   The ending Y coordinate of the polygon.
     * @param sides  The number of sides of the polygon.
     * @param gc     The GraphicsContext of the canvas.
     */
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

        // Draws the filled polygon.
        gc.fillPolygon(xPoints, yPoints, sides);
    }

    /**
     * Draws a semicircle.
     *
     * @param startX The starting X coordinate of the semicircle.
     * @param startY The starting Y coordinate of the semicircle.
     * @param endX   The ending X coordinate of the semicircle.
     * @param endY   The ending Y coordinate of the semicircle.
     * @param gc     The GraphicsContext of the canvas.
     */
    public void drawSemiCircle(Double startX, Double startY, Double endX, Double endY, GraphicsContext gc) {
        // Draws the filled half-circle.
        gc.fillArc(startX, startY, endX - startX, endY - startY, 0, 180, ArcType.ROUND);
    }
}