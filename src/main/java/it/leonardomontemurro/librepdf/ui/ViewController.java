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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewController {
    private final View view;
    private final DropView dropView;
    private final FileView fileView;
    private final FileChooser fileChooser = new FileChooser();
    private final Stage stage;
    private final ArrayList<File> pdfFiles = new ArrayList<>();

    public ViewController(Stage stage) {
        this.view = new View();
        this.stage = stage;

        this.view.setGlobalTheme();
        this.view.initializeScene();

        this.dropView = new DropView(view.getStackPane());

        this.dropView.initializeDragAndDropScene();
        this.dropView.dragAndDrop();

        this.fileView = new FileView();
        fileView.buildSideRight(view.getStackPane().widthProperty());

        this.view.setOnOperationSelected(this::onOperationChanged);
        this.dropView.setOnFilesDropped(this::onFilesDropped);
        this.dropView.setBackButtonAction(this::backToHome);
        this.dropView.setOnFileChooserAction(this::getFiles);

        initializeFileCardScene();
        initializeFileChooser();
        buildStackPane();
        backToHome();
    }

    private void getFiles () {
        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        if(files != null){
            pdfFiles.addAll(files);
            buildFlowPane();
        }
    }

    private void initializeFileCardScene() {
        view.addToStackPane(fileView.getBorderPane());
        fileView.setFileViewVisible(false);
        StackPane.setAlignment(fileView.getBorderPane(), Pos.CENTER);
    }

    private void buildFlowPane() {
        fileView.setFileViewVisible(true);
        int count = 1;
        for(File file : pdfFiles){
            fileView.buildCard(file, count);
            count++;
        }
        clearScene();
    }

    private void buildStackPane() {
        view.addToStackPane(dropView.getDragAndDropPane());
        view.addToStackPane(dropView.getTop());
        view.addToStackPane(dropView.getFileChooserButton());
        view.addToStackPane(dropView.getBackButton());

        StackPane.setAlignment(dropView.getBackButton(), Pos.TOP_LEFT);
        StackPane.setMargin(dropView.getBackButton(), new Insets(20, 0, 0, 20));
        dropView.setBackButtonToFront();
    }

    private void initializeFileChooser() {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Select PDF files!");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF","*.pdf")
        );
    }

    private void onFilesDropped(List<File> files) {
        pdfFiles.addAll(files);
        buildFlowPane();
    }

    private void onOperationChanged(PdfOperation operation) {
        dropView.setOperationTitle(operation.getName());
        fileView.setOperationName(operation.getName(), operation.getDescription());
        view.setHomeVisible(false);
        dropView.setDropViewSceneVisible(true);
    }

    private void clearScene() {
        dropView.setDragAndDropVisible(false);
        view.setHomeVisible(false);
    }

    private void clearFile() {
        fileView.clearFlowPane();
        pdfFiles.clear();
    }

    private void backToHome() {
        if(dropView.isDragAndDropPaneVisible()){
            dropView.setDropViewSceneVisible(false);
            view.setHomeVisible(true);
        } else {
            clearFile();
            fileView.setFileViewVisible(false);
            dropView.setDropViewSceneVisible(true);
        }
    }

    public View getView() {
        return view;
    }
}
