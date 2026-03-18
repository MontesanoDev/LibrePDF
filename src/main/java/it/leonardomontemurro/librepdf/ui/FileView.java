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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

import java.io.File;

public class FileView {
    private final BorderPane borderPane = new BorderPane();
    private final FlowPane flowPane = new FlowPane();
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox sideRight = new VBox();
    private final Button operationButton = new Button();
    private final Label operationName = new Label();
    private final Label descriptionName = new Label();
    private Runnable onOperationStared;

    public FileView() {
        initializeFileViewScene();
    }

    public void initializeFileViewScene() {
        buildFlowPane();
        buildScrollPane();
        buildBorderPane();
    }

    private void buildBorderPane() {
        borderPane.setCenter(scrollPane);
        borderPane.setRight(sideRight);
    }

    void buildSideRight(javafx.beans.property.ReadOnlyDoubleProperty parentWidth) {
        sideRight.setAlignment(Pos.CENTER);
        sideRight.prefWidthProperty().bind(parentWidth.divide(4));
        sideRight.minWidthProperty().bind(sideRight.prefWidthProperty());
        sideRight.maxWidthProperty().bind(sideRight.prefWidthProperty());
        sideRight.getStyleClass().add("sideBar");

        operationName.getStyleClass().add("operationName");
        descriptionName.setWrapText(true);

        operationButton.getStyleClass().add("operationButton");
        operationButton.setOnAction(_ -> onOperationStared.run());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sideRight.getChildren().addAll(operationName, descriptionName, spacer, operationButton);

        sideRight.setPadding(new Insets(50, 10, 50, 10));
    }

    private void buildScrollPane() {
        scrollPane.setContent(flowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);

        scrollPane.setPadding(new Insets(70,10,10,10));
    }

    private void buildFlowPane() {
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setHgap(30);
        flowPane.setVgap(30);
    }

    void buildCard(File file, int count){
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("fileCard");
        Region pdfIcon = new Region();
        pdfIcon.getStyleClass().add("pdfIcon");
        pdfIcon.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        Label fileNameLabel = new Label(file.getName());
        fileNameLabel.setWrapText(true);
        fileNameLabel.setAlignment(Pos.CENTER);
        fileNameLabel.setTextAlignment(TextAlignment.CENTER);
        fileNameLabel.getStyleClass().add("fileNameLabel");

        TextField textField = new TextField();
        textField.setAlignment(Pos.CENTER);
        textField.setText(String.valueOf(count));

        textField.getStyleClass().add("orderInput");

        card.getChildren().addAll(pdfIcon, fileNameLabel, textField);
        flowPane.getChildren().add(card);
    }

    void setOnOperationStared(Runnable callback) {
        this.onOperationStared = callback;
    }

    void setFileViewVisible(Boolean visible) {
        borderPane.setVisible(visible);
    }

    BorderPane getBorderPane() {
        return borderPane;
    }

    void clearFlowPane() {
        flowPane.getChildren().clear();
    }

    void setOperationName(String name, String description){
        operationButton.setText(name.toUpperCase());
        operationName.setText(name.toUpperCase());
        descriptionName.setText(description);
    }
}
