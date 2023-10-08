package com.niceferrari.photoapp;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class TabOperation {
    public TabOperation() {

    }

    public GraphicsContext tabSelectionChanged(Tab selectedTab, GraphicsContext gc) {
        if (selectedTab != null) {
            Node tabContent = selectedTab.getContent();

            if (tabContent instanceof ScrollPane scrollPane) {
                Node anchorPaneNode = scrollPane.getContent();

                if (anchorPaneNode instanceof AnchorPane anchorPane) {

                    if (!anchorPane.getChildren().isEmpty()) {
                        Node canvasNode = anchorPane.getChildren().get(0);

                        if (canvasNode instanceof Canvas selectedCanvas) {
                            gc = selectedCanvas.getGraphicsContext2D();
                        }
                    }
                }
            }
        }
        return gc;
    }
}
