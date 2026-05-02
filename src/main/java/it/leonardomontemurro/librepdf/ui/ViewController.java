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
package it.leonardomontemurro.librepdf.ui;

import it.leonardomontemurro.librepdf.PdfEngine;
import it.leonardomontemurro.librepdf.PdfOperation;
import it.leonardomontemurro.librepdf.util.FileService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


import static it.leonardomontemurro.librepdf.PdfOperation.MERGE;

public class ViewController {
    private final View view;
    private final DropView dropView;
    private final FileView fileView;
    private final ResultView resultView;
    private final PdfEngine pdfEngine;
    private final Stage stage;
    private final FileService fileService;
    private final List<File> pdfFiles = new ArrayList<>();
    private final List<TextField> textFields = new ArrayList<>();

    private PdfOperation currentOperation;

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

        this.resultView = new ResultView();

        this.pdfEngine = new PdfEngine();

        this.pdfEngine.setTheOperationCompleted(this::showProgress);

        this.view.setOnOperationSelected(this::onOperationChanged);
        this.dropView.setOnFilesDropped(this::onFilesDropped);
        this.dropView.setBackButtonAction(this::backScene);
        this.dropView.setOnFileChooserAction(this::getFiles);
        this.fileView.setOnOperationStared(this::onOperationStarted);
        this.resultView.setOnHomeSelected(this::backToHome);

        this.fileService = new FileService();
        this.fileService.initializeFileChooser();

        initializeFileCardScene();
        buildResultView();
        buildStackPane();
        bindBackButton();
        backScene();
    }

    private void bindBackButton() {
        dropView.getBackButton().disableProperty().bind(resultView.getButton().visibleProperty());
    }

    private void buildResultView() {
        view.addToStackPane(resultView.getSceneContainer());
        StackPane.setAlignment(resultView.getSceneContainer(), Pos.CENTER);
    }

    private void getFiles () {
        List<File> files = fileService.getFileChooser().showOpenMultipleDialog(stage);
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
        checkOperation();
        int count = 1;
        for(File file : pdfFiles){
            TextField textField = fileView.buildCard(file, count);
            textFields.add(textField);
            if(!(currentOperation == MERGE)) {
                textField.setEditable(false);
                textField.setCursor(Cursor.HAND);
            }
            count++;
        }
        clearScene();
    }

    private void checkOperation() {
        switch (currentOperation) {
            case METADATA -> fileView.setMetadataInfoVisible(true);
            case PROTECT -> {
                fileView.setPasswordFieldVisible(true);
                fileView.setUnlockFieldVisible(true);
            }
            case UNLOCK -> {
                fileView.setPasswordFieldVisible(true);
                fileView.setUnlockFieldVisible(false);
            }
            case PDFTOJPEG -> fileView.setConverterOptionsVisibile(true);
            case SPLIT -> fileView.setSplitOptionsVisible(true);
        }
    }

    private void buildStackPane() {
        view.addToStackPane(dropView.getDragAndDropPane());
        view.addToStackPane(dropView.getTop());
        view.addToStackPane(dropView.getFileChooserButton());
        view.addToStackPane(dropView.getBackButton());
        view.addToStackPane(resultView.getBackToHome());

        StackPane.setAlignment(dropView.getBackButton(), Pos.TOP_LEFT);
        StackPane.setMargin(dropView.getBackButton(), new Insets(20, 0, 0, 20));
        StackPane.setAlignment(resultView.getBackToHome(), Pos.TOP_RIGHT);
        StackPane.setMargin(resultView.getBackToHome(), new Insets(20, 20, 0, 20));
        dropView.setBackButtonToFront();
        resultView.getButton().toFront();
    }

    private void onFilesDropped(List<File> files) {
        pdfFiles.addAll(files);
        buildFlowPane();
    }

    private void showProgress() {
        resultView.showProgress();
        clearScene();
    }

    private void onOperationStarted() {
        if (currentOperation == PdfOperation.MERGE) {
            if (!fileService.orderFiles(pdfFiles, textFields)) {
                return;
            }
        }

        switch (currentOperation) {
            case METADATA -> pdfEngine.editMetadata(
                    pdfFiles,
                    fileView.getMetadataTitle(),
                    fileView.getMetadataAuthor(),
                    fileView.getMetadataKeywords(),
                    fileView.isNuclearMetadata()
            );
            case PROTECT -> pdfEngine.protectFile(pdfFiles, fileView.getPassword());
            case UNLOCK -> pdfEngine.unprotectFile(pdfFiles, fileView.getPassword());
            case PDFTOJPEG -> pdfEngine.convertToJpeg(pdfFiles, fileView.getDpi());
            case MERGE -> pdfEngine.mergeFile(pdfFiles);
            case SPLIT -> {
                if(fileView.isSplitAllPagesSelected()) {
                    pdfEngine.splitFile(pdfFiles, null, fileView.isSplitAllPagesSelected());
                } else {
                    pdfEngine.splitFile(pdfFiles, fileView.getSplitRange(), fileView.isSplitAllPagesSelected());
                }
            }
        }
    }

    private void onOperationChanged(PdfOperation operation) {
        dropView.setOperationTitle(operation.getName());
        fileView.setOperationName(operation.getName(), operation.getDescription());
        view.setHomeVisible(false);
        dropView.setDropViewSceneVisible(true);

        currentOperation = operation;
    }

    private void clearScene() {
        dropView.setDragAndDropVisible(false);
        view.setHomeVisible(false);
        fileView.getBorderPane().setVisible(!resultView.isSceneVisible());
    }

    private void clearFile() {
        fileView.clearFlowPane();
        pdfFiles.clear();
        textFields.clear();
        fileView.clearPassword();
    }

    private void backScene() {
        if(dropView.isDragAndDropPaneVisible()){
            dropView.setDropViewSceneVisible(false);
            view.setHomeVisible(true);
        } else if(resultView.isSceneVisible()) {
            resultView.hideScene();
            clearScene();
        } else {
            clearFile();
            fileView.hideInputFields();
            fileView.setFileViewVisible(false);
            dropView.setDropViewSceneVisible(true);
        }
    }

    private void backToHome(){
        clearScene();
        clearFile();
        fileView.setFileViewVisible(false);
        dropView.setBackButtonVisible(false);
        resultView.hideScene();
        view.setHomeVisible(true);
    }

    public View getView() {
        return view;
    }
}
