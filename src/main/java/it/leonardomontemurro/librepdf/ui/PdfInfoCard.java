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
import it.leonardomontemurro.librepdf.util.I18N;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PdfInfoCard extends VBox {

    public PdfInfoCard(PdfInfoData data) {
        this.setSpacing(8);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("infoCard");

        Label fileName = new Label("📄 " + data.fileName());
        fileName.getStyleClass().add("cardTitle");

        if (data.passwordRequired()) {
            getChildren().addAll(
                fileName,
                row(I18N.get("pdfinfo.size"), humanSize(data.fileSizeBytes())),
                row(I18N.get("pdfinfo.encrypted"), I18N.get("pdfinfo.yes"))
            );
            this.getStyleClass().add("infoCardPasswordRequired");
            return;
        }

        getChildren().addAll(
            fileName,
            row(I18N.get("pdfinfo.size"), humanSize(data.fileSizeBytes())),
            row(I18N.get("pdfinfo.pages"), String.valueOf(data.pages())),
            row(I18N.get("pdfinfo.version"), nullSafe(data.pdfVersion())),
            row(I18N.get("pdfinfo.title"), nullSafe(data.title())),
            row(I18N.get("pdfinfo.author"), nullSafe(data.author())),
            row(I18N.get("pdfinfo.subject"), nullSafe(data.subject())),
            row(I18N.get("pdfinfo.keywords"), nullSafe(data.keywords())),
            row(I18N.get("pdfinfo.creator"), nullSafe(data.creator())),
            row(I18N.get("pdfinfo.producer"), nullSafe(data.producer())),
            row(I18N.get("pdfinfo.creationDate"), nullSafe(data.creationDate())),
            row(I18N.get("pdfinfo.modificationDate"), nullSafe(data.modificationDate())),
            row(I18N.get("pdfinfo.encrypted"), data.encrypted() ? I18N.get("pdfinfo.yes") : I18N.get("pdfinfo.no")),
            row(I18N.get("pdfinfo.javascript"), data.hasJavaScript() ? I18N.get("pdfinfo.present") : I18N.get("pdfinfo.notPresent")),
            row(I18N.get("pdfinfo.attachments"), data.hasAttachments() ? I18N.get("pdfinfo.present") : I18N.get("pdfinfo.notPresent")),
            row(I18N.get("pdfinfo.formFields"), data.hasFormFields() ? I18N.get("pdfinfo.present") : I18N.get("pdfinfo.notPresent")),
            row(I18N.get("pdfinfo.annotations"), data.hasAnnotations() ? I18N.get("pdfinfo.present") : I18N.get("pdfinfo.notPresent"))
        );
        if (data.hasJavaScript()) {
            this.getStyleClass().add("infoCardWarning");
        }
    }

    private HBox row(String key, String value) {
        Label k = new Label(key + ":");
        Label v = new Label(value);
        HBox hBox = new HBox(8, k, v);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private String nullSafe(String s) {
        return s == null || s.isBlank() ? "—" : s;
    }

    private String humanSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
}
