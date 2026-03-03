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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

import java.io.File;

public class FileView {
    private final FlowPane flowPane = new FlowPane();
    private final ScrollPane scrollPane = new ScrollPane();

    public FileView() {
        initializeFileViewScene();
    }

    public void initializeFileViewScene() {
        buildFlowPane();
        buildScrollPane();
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
    FlowPane getFlowPane() {
        return flowPane;
    }

    ScrollPane getScrollPane() {
        return scrollPane;
    }
}
