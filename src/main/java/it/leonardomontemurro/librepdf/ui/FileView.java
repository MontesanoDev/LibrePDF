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

import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;

public class FileView {
    private final TilePane tilePane = new TilePane();

    public FileView() {
        initializeFileViewScene();
    }

    public void initializeFileViewScene() {
        buildFlowPane();
        //TODO ScrollPane
    }

    private void buildFlowPane() {
        tilePane.setAlignment(Pos.CENTER);
        tilePane.setPrefColumns(5);
        tilePane.setMaxWidth(Region.USE_PREF_SIZE);
        tilePane.setHgap(30);
        tilePane.setVgap(30);
    }

    TilePane getTilePane() {
        return tilePane;
    }
}
