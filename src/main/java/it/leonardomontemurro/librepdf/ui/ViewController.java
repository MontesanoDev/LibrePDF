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

public class ViewController {
    private final View view;
    private final DropView dropView;

    public ViewController() {
        this.view = new View();
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

        backToHome();
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
