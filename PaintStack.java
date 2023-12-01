package com.niceferrari.photoapp;

import java.util.Stack;

/**
 * Manages the snapshots of the canvas in a stack.
 *
 * @see CanvasSnapshot
 * @see PhotoController
 * @see Stack
 */
public class PaintStack {
        private Stack<CanvasSnapshot> undoStack = new Stack<>();
        private Stack<CanvasSnapshot> redoStack = new Stack<>();

        /**
         * Pushes a snapshot of the canvas to the undo stack and clears the redo stack.
         *
         * @param snapshot The snapshot of the canvas.
         */
        public void pushSnapshot(CanvasSnapshot snapshot) {
            undoStack.push(snapshot);
            redoStack.clear();
        }

        /**
         * Pops a snapshot of the canvas from the undo stack.
         *
         * @return The snapshot of the canvas.
         */
        public CanvasSnapshot popUndo() {
            if (!undoStack.isEmpty()) {
                CanvasSnapshot snapshot = undoStack.pop();
                redoStack.push(snapshot);
                return snapshot;
            }
            return null;
        }

        /**
         * Pops a snapshot of the canvas from the redo stack.
         *
         * @return The snapshot of the canvas.
         */
        public CanvasSnapshot popRedo() {
            if (!redoStack.isEmpty()) {
                CanvasSnapshot snapshot = redoStack.pop();
                undoStack.push(snapshot);
                return snapshot;
            }
            return null;
        }

        /**
         * Checks if the user can undo.
         *
         * @return False if the undo stack is empty, true otherwise.
         */
        public boolean canUndo() {
            return !undoStack.isEmpty();
        }

        /**
         * Checks if the user can redo.
         *
         * @return False if the redo stack is empty, true otherwise.
         */
        public boolean canRedo() {
            return !redoStack.isEmpty();
        }

        /**
         * Returns the undo stack.
         */
        public Stack<CanvasSnapshot> getUndoStack() {
            return undoStack;
        }

        /**
         * Returns the redo stack.
         */
        public Stack<CanvasSnapshot> getRedoStack() {
            return redoStack;
        }
}