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
package it.leonardomontemurro.librepdf;

public class ViewController {
    private final View view;
    private PdfOperation currentOperation;

    public ViewController() {
        this.view = new View();

        this.view.setGlobalTheme();
        this.view.initializeScene();

        this.view.setOnOperationSelected(this::onOperationChanged);
    }

    public View getView() {
        return view;
    }

    private void onOperationChanged(PdfOperation operation) {
        this.currentOperation = operation;

    }
}
