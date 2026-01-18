/*
 * LibrePDF - A lightweight, native tool for manipulating PDF files.
 * Copyright (C) 2026 Leonardo Montemurro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package it.leonardomontemurro.localpdf;

public class Handler {

    private final Icons currentOperation;
    private final View view;

    public Handler(Icons currentOperation, View view) {
        this.currentOperation = currentOperation;
        this.view = view;
    }

    public void buildAction(){
        view.buildDragAndDropScene();
        dragAndDrop();
    }

    private void dragAndDrop(){
        view.getStackPane().setOnDragEntered(entered -> {
            view.getDragAndDropPane().getStyleClass().add("dragOver");
            entered.consume();
        });
        view.getStackPane().setOnDragExited(exited -> {
            view.getDragAndDropPane().getStyleClass().remove("dragOver");
            exited.consume();
        });
        view.getStackPane().setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        view.getStackPane().setOnDragDropped(event -> {
            var db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                for (java.io.File file : db.getFiles()) {
                    if (file.getName().toLowerCase().endsWith(".pdf")) {
                        System.out.println("PDF Found: " + file.getAbsolutePath());
                        success = true;
                    } else {
                        System.out.println("I can't find PDF:" + file.getAbsolutePath());
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public Icons getCurrentOperation() {
        return currentOperation;
    }
}
