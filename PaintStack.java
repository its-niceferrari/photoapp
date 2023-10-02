package com.niceferrari.photoapp;

import java.util.Stack;

public class PaintStack {
        private Stack<CanvasSnapshot> undoStack = new Stack<>();
        private Stack<CanvasSnapshot> redoStack = new Stack<>();

        public void pushSnapshot(CanvasSnapshot snapshot) {
            undoStack.push(snapshot);
            redoStack.clear();
        }

        public CanvasSnapshot popUndo() {
            if (!undoStack.isEmpty()) {
                CanvasSnapshot snapshot = undoStack.pop();
                redoStack.push(snapshot);
                return snapshot;
            }
            return null;
        }

        public CanvasSnapshot popRedo() {
            if (!redoStack.isEmpty()) {
                CanvasSnapshot snapshot = redoStack.pop();
                undoStack.push(snapshot);
                return snapshot;
            }
            return null;
        }

        public boolean canUndo() {
            return !undoStack.isEmpty();
        }

        public boolean canRedo() {
            return !redoStack.isEmpty();
        }
}