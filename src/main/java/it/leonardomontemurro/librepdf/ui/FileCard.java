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

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.converter.IntegerStringConverter;

public class FileCard extends VBox{

    private final TextField textField = new TextField();

    public FileCard(String fileName, int count) {

        this.setSpacing(8);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("fileCard");

        Region pdfIcon = new Region();
        pdfIcon.getStyleClass().add("pdfIcon");
        pdfIcon.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        Label fileNameLabel = new Label(fileName);
        fileNameLabel.setWrapText(true);
        fileNameLabel.setAlignment(Pos.CENTER);
        fileNameLabel.setTextAlignment(TextAlignment.CENTER);
        fileNameLabel.getStyleClass().add("fileNameLabel");

        this.textField.setAlignment(Pos.CENTER);
        this.textField.getStyleClass().add("orderInput");
        bindIntegersToTextField(count);
        this.getChildren().addAll(pdfIcon, fileNameLabel, textField);
    }
    //Thank you javathinking for this code <3
    private void bindIntegersToTextField(int count) {
        TextFormatter<Integer> integerFormatter = new TextFormatter<>(
            new IntegerStringConverter(),
            count,
    change -> {
                String newText = change.getControlNewText();
                if (newText.matches("\\d*")) {
                    return change;
                }
                return null;
            }
        );
        this.textField.setTextFormatter(integerFormatter);
    }

    TextField getTextFields() {
        return textField;
    }
}
