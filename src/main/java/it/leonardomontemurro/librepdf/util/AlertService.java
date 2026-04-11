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

package it.leonardomontemurro.librepdf.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertService {

    private static final String APP_TITLE = "LibrePDF";

    public static void info(String message) {
        executeAlert(AlertType.INFORMATION, "Information", message);
    }

    public static void warning(String message) {
        executeAlert(AlertType.WARNING, I18N.get("alert.title.warning"), message);
    }

    public static void error(String message) {
        executeAlert(AlertType.ERROR, I18N.get("alert.title.error"), message);
    }

    private static void executeAlert(AlertType type, String title, String content) {
        if (Platform.isFxApplicationThread()) {
            showAlert(type, title, content);
        } else {
            Platform.runLater(() -> showAlert(type, title, content));
        }
    }
    
    private static void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(APP_TITLE + " - " + title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
