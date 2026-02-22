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

import it.leonardomontemurro.librepdf.PdfOperation;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

public class ViewController {
    private final View view;
    private final DropView dropView;
    private final FileChooser fileChooser = new FileChooser();
    private final Stage stage;

    public ViewController(Stage stage) {
        this.view = new View();
        this.stage = stage;

        this.dropView = new DropView(view.getStackPane());
        this.view.setGlobalTheme();
        this.view.initializeScene();

        this.dropView.initializeDragAndDropScene();
        this.dropView.dragAndDrop();

        view.getStackPane().getChildren().add(dropView.getDragAndDropPane());
        view.getStackPane().getChildren().add(dropView.getTop());
        view.getStackPane().getChildren().add(dropView.getFileChooserButton());
        view.getBackButton().toFront();

        this.view.setOnOperationSelected(this::onOperationChanged);

        this.view.getBackButton().setOnAction(_ -> backToHome());

        this.dropView.getFileChooserButton().setOnAction(_ -> getFiles());

        initializeFileChooser();
        backToHome();
    }

    private void getFiles () {
        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        if(files != null){
            for(File file : files){
                System.out.println("Pdf trovato: " + file.getAbsolutePath());
            }
        }
    }

    private void initializeFileChooser() {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Select PDF files!");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF","*.pdf")
        );
    }

    private void onOperationChanged(PdfOperation operation) {
        this.dropView.setOperationTitle(operation.getName());
        setHomeVisible(false);
        setDragAndDropVisible(true);
    }

    private void backToHome() {
        setDragAndDropVisible(false);
        setHomeVisible(true);
    }

    private void setDragAndDropVisible(Boolean visible) {
        dropView.getDragAndDropPane().setVisible(visible);
        dropView.getTop().setVisible(visible);
        dropView.getFileChooserButton().setVisible(visible);
        view.getBackButton().setVisible(visible);
    }

    private void setHomeVisible(Boolean visible) {
        view.getGridPane().setVisible(visible);
        view.getGridPane().setDisable(!visible);
    }

    public View getView() {
        return view;
    }
}
