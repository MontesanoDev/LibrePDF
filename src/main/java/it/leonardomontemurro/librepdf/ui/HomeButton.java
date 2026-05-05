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

import it.leonardomontemurro.librepdf.PdfOperation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;

public class HomeButton extends Button {

    public HomeButton(PdfOperation icon, double mainWidth, double mainHeight){

        this.getStyleClass().add("homeButton");
        this.setWrapText(true);
        this.setTextAlignment(TextAlignment.CENTER);
        this.setPickOnBounds(false);

        Region region = new Region();
        region.getStyleClass().add(icon.name().toLowerCase());

        this.setContentDisplay(ContentDisplay.TOP);
        this.setGraphic(region);
        this.setText(icon.getDescription());

        this.setPrefSize(mainWidth * 0.2, mainHeight * 0.3);
    }
}
