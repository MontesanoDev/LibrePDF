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

import it.leonardomontemurro.librepdf.util.I18N;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class FileView {
    private final BorderPane borderPane = new BorderPane();
    private final FlowPane flowPane = new FlowPane();
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox sideRight = new VBox();
    private final Button operationButton = new Button();
    private final Label operationName = new Label();
    private final Label descriptionName = new Label();
    private final VBox metadataFields = new VBox(8);
    private final TextField author = new TextField();
    private final TextField title = new TextField();
    private final TextField keywords = new TextField();
    private final CheckBox nuclearMetadata = new CheckBox();
    private final VBox passwordField = new VBox(15);
    private final PasswordField password = new PasswordField();
    private final PasswordField confirmPassword = new PasswordField();

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
        buildSideBar(parentWidth);

        operationName.getStyleClass().add("operationName");
        descriptionName.setWrapText(true);
        buildMetadataInputFields();
        buildPasswordInputFields();
        operationButton.getStyleClass().add("operationButton");
        operationButton.setText(I18N.get("ui.execute"));
        operationButton.setOnAction(_ -> onOperationStared.run());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Region topSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);

        sideRight.getChildren().addAll(operationName, descriptionName, topSpacer, metadataFields,
                passwordField, spacer, operationButton);

        sideRight.setPadding(new Insets(50, 20, 50, 20));
    }

    private void buildSideBar(javafx.beans.property.ReadOnlyDoubleProperty parentWidth) {
        sideRight.setAlignment(Pos.CENTER);
        sideRight.prefWidthProperty().bind(parentWidth.divide(4));
        sideRight.minWidthProperty().bind(sideRight.prefWidthProperty());
        sideRight.maxWidthProperty().bind(sideRight.prefWidthProperty());
        sideRight.setPadding(new Insets(0,50,0,50));
        sideRight.getStyleClass().add("sideBar");
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

    private void buildPasswordInputFields() {
        password.setPromptText(I18N.get("ui.password.prompt"));
        password.setAlignment(Pos.CENTER);
        confirmPassword.setPromptText(I18N.get("ui.password.confirm"));
        confirmPassword.setAlignment(Pos.CENTER);
        passwordField.getChildren().addAll(password, confirmPassword);
        passwordField.setPadding(new Insets(0,20,0,20));
        passwordField.setVisible(false);
        passwordField.managedProperty().bind(passwordField.visibleProperty());
    }

    private void buildMetadataInputFields() {
        title.setAlignment(Pos.CENTER);
        title.setPromptText(I18N.get("ui.metadata.title"));
        author.setAlignment(Pos.CENTER);
        author.setPromptText(I18N.get("ui.metadata.author"));
        keywords.setAlignment(Pos.CENTER);
        keywords.setPromptText(I18N.get("ui.metadata.keywords"));

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 20, 0, 0));
        hbox.setAlignment(Pos.BASELINE_RIGHT);
        hbox.setSpacing(10);

        Label nuclearLabel = new Label(I18N.get("ui.metadata.clean_all"));
        nuclearMetadata.setCursor(Cursor.HAND);
        hbox.getChildren().addAll(nuclearLabel, nuclearMetadata);

        metadataFields.setPadding(new Insets(0,20,0,20));
        metadataFields.setSpacing(15);
        metadataFields.getChildren().addAll(title,author,keywords,hbox);
        metadataFields.setVisible(false);
        metadataFields.managedProperty().bind(metadataFields.visibleProperty());

        addListenerInputFields();
    }

    private void addListenerInputFields() {
        nuclearMetadata.selectedProperty().addListener((_, _, newValue) -> {
            if (newValue) {
                title.setDisable(true);
                author.setDisable(true);
                keywords.setDisable(true);
            } else {
                title.setDisable(false);
                author.setDisable(false);
                keywords.setDisable(false);
            }
        });
    }

    void hideInputFields() {
        setPasswordFieldVisible(false);
        setMetadataInfoVisible(false);
    }

    void setPasswordFieldVisible(Boolean visible) {
        passwordField.setVisible(visible);
    }

    void setMetadataInfoVisible(Boolean visible) {
        metadataFields.setVisible(visible);
    }

    void buildCard(String fileName, int count){
        flowPane.getChildren().add(new FileCard(fileName, count));
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
        operationName.setText(name.toUpperCase());
        descriptionName.setText(description);
    }
}
