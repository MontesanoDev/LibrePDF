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
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.Region;

public class HomeButton extends Button {

    private final static int BUTTON_WIDTH = 240;
    private final static int BUTTON_HEIGHT = 210;

    public HomeButton(PdfOperation icon){

        this.getStyleClass().add("homeButton");
        this.setWrapText(true);
        this.setPickOnBounds(false);

        Region region = new Region();
        region.getStyleClass().add(icon.name().toLowerCase());

        this.setContentDisplay(ContentDisplay.TOP);
        this.setGraphic(region);
        this.setText(icon.getDescription());

        this.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
    }
}
