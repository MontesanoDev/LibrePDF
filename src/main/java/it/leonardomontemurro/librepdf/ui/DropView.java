/*
 *
 *  * LibrePDF - A lightweight, native tool for manipulating PDF files.
 *  * Copyright (C) 2026 Leonardo Montemurro
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package it.leonardomontemurro.librepdf.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DropView {

    private final Label dragAndDropInfo = new Label();
    private final Pane dragAndDropPane = new Pane();
    private final Label top = new Label();
    private final Button fileChooserButton = new Button();
    private final StackPane stackPane;
    private final Button backButton = new Button();
    private Consumer<List<File>> onFilesDropped;

    public DropView(StackPane viewStackPane) {
        this.stackPane = viewStackPane;
    }

    public void initializeDragAndDropScene() {
        buildDragArea();
        buildTop();
        buildFileChooserButton();
        buildBackButton();
        StackPane.setAlignment(fileChooserButton, Pos.CENTER);
        StackPane.setAlignment(top, Pos.TOP_CENTER);
        StackPane.setMargin(top, new Insets(30, 0, 0, 0));
    }

    public void setOperationTitle(String title) {
        top.setText(title.toUpperCase());
    }

    public void dragAndDrop() {
        stackPane.setOnDragEntered(entered -> {
            if(dragAndDropPane.isVisible()) {
                dragAndDropPane.getStyleClass().add("dragOver");
            }
            entered.consume();
        });
        stackPane.setOnDragExited(exited -> {
            if(!dragAndDropPane.isVisible()) {
                dragAndDropPane.getStyleClass().remove("dragOver");
            }
            exited.consume();
        });
        stackPane.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles() && dragAndDropPane.isVisible()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        stackPane.setOnDragDropped(event -> {
            if(dragAndDropPane.isVisible()) {
                var db = event.getDragboard();
                List<File> pdfs = new ArrayList<>();
                boolean success = false;
                if (db.hasFiles()) {
                    for (File file : db.getFiles()) {
                        if (file.getName().toLowerCase().endsWith(".pdf")) {
                            pdfs.add(file);
                            success = true;
                        }
                    }
                }
                event.setDropCompleted(success);
                event.consume();
                onFilesDropped.accept(pdfs);
            }
        });
    }

    private void buildFileChooserButton() {
        fileChooserButton.maxHeightProperty().bind(dragAndDropPane.heightProperty().divide(7));
        fileChooserButton.maxWidthProperty().bind(dragAndDropPane.heightProperty().divide(3));
        fileChooserButton.setText("Select PDF Files!");
        fileChooserButton.setWrapText(true);
        fileChooserButton.setPickOnBounds(false);
        fileChooserButton.setTextAlignment(TextAlignment.CENTER);
        fileChooserButton.getStyleClass().add("addFilesButton");
        fileChooserButton.translateYProperty().bind(dragAndDropPane.heightProperty().divide(4));
    }

    private void buildDragArea() {
        dragAndDropPane.maxWidthProperty().bind(stackPane.widthProperty().multiply(0.35));
        dragAndDropPane.maxHeightProperty().bind(stackPane.heightProperty().multiply(0.55));
        dragAndDropPane.getChildren().add(setInfo());
        dragAndDropPane.getStyleClass().add("dragAndDropArea");
    }

    private void buildTop() {
        top.getStyleClass().add("top-label");
        top.setAlignment(Pos.CENTER);
        top.setMaxWidth(Double.MAX_VALUE);
        top.setTextAlignment(TextAlignment.CENTER);
    }

    private Label setInfo(){
        dragAndDropInfo.setText("Drag and drop PDF files here!");
        dragAndDropInfo.getStyleClass().add("dragAndDropInfo");
        dragAndDropInfo.maxWidthProperty().bind(dragAndDropPane.widthProperty().divide(1));
        dragAndDropInfo.maxHeightProperty().bind(dragAndDropPane.heightProperty().divide(2));
        dragAndDropInfo.setAlignment(Pos.CENTER);
        dragAndDropInfo.setTextAlignment(TextAlignment.CENTER);
        dragAndDropInfo.setWrapText(true);
        dragAndDropInfo.layoutXProperty().bind(dragAndDropPane.widthProperty().subtract(dragAndDropInfo.widthProperty()).divide(2));
        dragAndDropInfo.layoutYProperty().bind(dragAndDropPane.heightProperty().subtract(dragAndDropInfo.heightProperty()).divide(3));
        return dragAndDropInfo;
    }

    void setDragAndDropVisible(Boolean visible) {
        dragAndDropPane.setVisible(visible);
        top.setVisible(visible);
        fileChooserButton.setVisible(visible);
    }

    private void buildBackButton() {
        backButton.setText("← Back");
        backButton.getStyleClass().add("backButton");
        backButton.setVisible(false);
    }

    Button getBackButton() {
        return backButton;
    }

    void setDropViewSceneVisible(Boolean visibile) {
        setBackButtonVisibile(visibile);
        setDragAndDropVisible(visibile);
    }

    void setBackButtonVisibile(Boolean visible){
        backButton.setVisible(visible);
    }

    void setOnFilesDropped(Consumer<List<File>> callback) {
        this.onFilesDropped = callback;
    }

    public Button getFileChooserButton() {
        return fileChooserButton;
    }

    public Label getTop() {
        return top;
    }

    public Pane getDragAndDropPane() {
        return dragAndDropPane;
    }
}
