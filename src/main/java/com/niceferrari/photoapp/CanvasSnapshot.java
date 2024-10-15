package com.niceferrari.photoapp;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 * Handles saving and restoring snapshots of the Canvas.
 *
 * @see Canvas
 * @see GraphicsContext
 * @see Image
 * @see WritableImage
 * @see SnapshotParameters
 */
public class CanvasSnapshot {
    private Image snapshot;

    /**
     * Creates a CanvasSnapshot object.
     *
     * @param canvas The Canvas to be saved.
     */
    public CanvasSnapshot(Canvas canvas) {
        this.snapshot = snapshotCanvas(canvas);
    }

    /**
     * Restores the Canvas to the saved snapshot.
     *
     * @param canvas The Canvas to be restored.
     */
    public void restoreSnapshot(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(snapshot, 0, 0);
    }

    /**
     * Creates a snapshot of the Canvas.
     *
     * @param canvas The Canvas to be saved.
     * @return The snapshot of the Canvas.
     */
    private Image snapshotCanvas(Canvas canvas) {
        SnapshotParameters params = new SnapshotParameters();
        WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(params, snapshot);
        return snapshot;
    }
}