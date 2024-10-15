package com.niceferrari.photoapp;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

/**
 * Retrieves the GraphicsContext of the selected tab.
 *
 * @see GraphicsContext
 * @see Tab
 * @see Node
 */
public class TabOperation {
    public TabOperation() {

    }

    /**
     * Retrieves the GraphicsContext of the selected tab.
     *
     * @param selectedTab The tab that is currently selected.
     * @param gc          The GraphicsContext of the selected tab.
     * @return The GraphicsContext of the selected tab.
     */
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