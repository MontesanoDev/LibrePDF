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

import it.leonardomontemurro.librepdf.core.PdfInfoData;
import it.leonardomontemurro.librepdf.util.FileService;
import it.leonardomontemurro.librepdf.util.I18N;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;

public class ResultView {

    private final VBox sceneContainer = new VBox(30);
    private final ScrollPane scrollPane = new ScrollPane();
    private final FlowPane flowPane = new FlowPane();
    private final Button openExplorer = new Button();
    private final Button backToHome = new Button();
    private final ProgressIndicator progressIndicator = new ProgressIndicator();
    private Runnable onHomeSelected;
    private File outputDirectory;

    public ResultView() {
        buildScene();
        buildPdfInfo();
        buildFlowPane();
    }

    private void buildScene() {
        Label label = new Label(I18N.get("result.info"));
        label.getStyleClass().add("LabelRenderedFile");
        label.visibleProperty().bind(progressIndicator.visibleProperty().not().and(scrollPane.visibleProperty().not()));
        label.managedProperty().bind(progressIndicator.visibleProperty().not().and(scrollPane.visibleProperty().not()));

        openExplorer.setText(I18N.get("result.button"));
        openExplorer.getStyleClass().add("buttonRenderedFile");
        openExplorer.visibleProperty().bind(progressIndicator.visibleProperty().not().and(scrollPane.visibleProperty().not()));
        openExplorer.minHeightProperty().bind(sceneContainer.widthProperty().divide(24));
        openExplorer.maxHeightProperty().bind(sceneContainer.widthProperty().divide(20));
        openExplorer.minWidthProperty().bind(sceneContainer.widthProperty().divide(10));
        openExplorer.managedProperty().bind(progressIndicator.visibleProperty().not().and(scrollPane.visibleProperty().not()));

        openExplorer.setOnAction(_ -> FileService.openExplorer(outputDirectory));
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

    private void buildPdfInfo() {
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setPadding(new Insets(70,10,10,10));

        scrollPane.managedProperty().bind(scrollPane.visibleProperty());
        scrollPane.setVisible(false);

        sceneContainer.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    void setPdfInfoVisible() {
        scrollPane.setVisible(true);
    }

    void buildFlowPane() {
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setHgap(30);
        flowPane.setVgap(30);

        scrollPane.setContent(flowPane);
    }

    void buildInfoCards(List<PdfInfoData> data) {
        flowPane.getChildren().clear();
        for (PdfInfoData d : data) {
            flowPane.getChildren().add(new PdfInfoCard(d));
        }
    }

    void start() {
        sceneContainer.setVisible(true);
        progressIndicator.setVisible(true);
    }

    void complete(File outputDirectory) {
        this.outputDirectory = outputDirectory;
        progressIndicator.setVisible(false);
    }

    void hideScene() {
        sceneContainer.setVisible(false);
        scrollPane.setVisible(false);
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

    ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

}
