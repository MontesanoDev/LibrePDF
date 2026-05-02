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

import it.leonardomontemurro.librepdf.util.FileService;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

public class ResultView {

    private final VBox sceneContainer = new VBox(30);
    private final Button openExplorer = new Button();
    private final Button backToHome = new Button();
    private final ProgressIndicator progressIndicator = new ProgressIndicator();
    private Runnable onHomeSelected;

    public ResultView() {
        buildScene();
    }

    private void buildScene() {
        Label label = new Label("Operazione Conclusa!");
        label.getStyleClass().add("LabelRenderedFile");
        label.visibleProperty().bind(progressIndicator.visibleProperty().not());
        label.managedProperty().bind(progressIndicator.visibleProperty().not());
        openExplorer.setText("Apri percorso file");
        openExplorer.getStyleClass().add("buttonRenderedFile");
        openExplorer.visibleProperty().bind(progressIndicator.visibleProperty().not());
        openExplorer.minHeightProperty().bind(sceneContainer.widthProperty().divide(24));
        openExplorer.maxHeightProperty().bind(sceneContainer.widthProperty().divide(20));
        openExplorer.minWidthProperty().bind(sceneContainer.widthProperty().divide(10));
        openExplorer.managedProperty().bind(progressIndicator.visibleProperty().not());
        openExplorer.setOnAction(_ -> FileService.openExplorer());

        backToHome.setText("Home ->");
        backToHome.getStyleClass().add("backButton");
        backToHome.visibleProperty().bind(sceneContainer.visibleProperty());
        backToHome.disableProperty().bind(progressIndicator.visibleProperty());
        backToHome.setOnAction(_ -> onHomeSelected.run());

        sceneContainer.setAlignment(Pos.CENTER);
        sceneContainer.getChildren().addAll(label, progressIndicator, openExplorer);

        progressIndicator.setVisible(false);
        sceneContainer.setVisible(false);
    }

    void showProgress() {
        if(!sceneContainer.isVisible()) {
            sceneContainer.setVisible(true);
            progressIndicator.setVisible(true);
        } else{
            progressIndicator.setVisible(false);
        }
    }

    void hideScene() {
        sceneContainer.setVisible(false);
    }

    boolean isSceneVisible() {
        return sceneContainer.isVisible();
    }

    VBox getSceneContainer() {
        return sceneContainer;
    }

    Button getBackToHome() {
        return backToHome;
    }

    void setOnHomeSelected(Runnable callback) {
        this.onHomeSelected = callback;
    }

    ProgressIndicator getButton() {
        return progressIndicator;
    }

}
