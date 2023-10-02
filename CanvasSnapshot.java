package com.niceferrari.photoapp;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class CanvasSnapshot {
    private Image snapshot;

    public CanvasSnapshot(Canvas canvas) {
        this.snapshot = snapshotCanvas(canvas);
    }

    public void restoreSnapshot(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(snapshot, 0, 0);
    }

    private Image snapshotCanvas(Canvas canvas) {
        SnapshotParameters params = new SnapshotParameters();
        WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(params, snapshot);
        return snapshot;
    }
}