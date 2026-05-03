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

import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileService {

    private final FileChooser fileChooser = new FileChooser();

    public static String getUniqueFilePath(String directory, String prefix) {
        File file = new File(directory, prefix + ".pdf");
        int counter = 1;

        while (file.exists()) {
            file = new File(directory, prefix + " (" + counter + ").pdf");
            counter++;
        }
        return file.getAbsolutePath();
    }

    public static File createDir(File pdf, String cleanName, String prefix) {
        String parent = pdf.getParent();
        File outputDir = new File(parent, cleanName + prefix);

        int counter = 1;
        while (outputDir.exists()) {
            outputDir = new File(parent, cleanName + "(" + counter + ")" + prefix);
            counter++;
        }

        boolean created = outputDir.mkdirs();
        if (!created) {
            AlertService.error(I18N.get("alert.create.directory.error"));
            throw new RuntimeException(I18N.get("alert.create.directory.error") + outputDir.getAbsolutePath());
        }
        return outputDir;
    }

    public static void openDefaultPdfViewer(String path) {
        if (Desktop.isDesktopSupported()) {
            try {
                File pdfDocument = new File(path);
                Desktop.getDesktop().open(pdfDocument);
            } catch (IOException e) {
                AlertService.error(I18N.get("alert.open.pdf.error"));
            }
        }
    }

    public void initializeFileChooser() {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle(I18N.get("select.files"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF","*.pdf")
        );
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    public boolean orderFiles(List<File> pdfFiles, List<TextField> textFields) {
        int size = pdfFiles.size();
        Set<Integer> seen = new HashSet<>();

        for (javafx.scene.control.TextField textField : textFields) {
            int val;
            try {
                val = Integer.parseInt(textField.getText());
            } catch (NumberFormatException e) {
                AlertService.warning(I18N.get("alert.order.empty"));
                return false;
            }
            if (val < 1 || val > size) {
                AlertService.warning(I18N.get("alert.order.outofrange"));
                return false;
            }
            if (!seen.add(val)) {
                AlertService.warning(I18N.get("alert.order.duplicate"));
                return false;
            }
        }

        List<File> orderedFiles = new ArrayList<>();
        for (TextField textField : textFields) {
            orderedFiles.add(pdfFiles.get(Integer.parseInt(textField.getText()) - 1));
        }

        if (!orderedFiles.equals(pdfFiles)) {
            pdfFiles.clear();
            pdfFiles.addAll(orderedFiles);
        }
        return true;
    }

    public static void openExplorer(File directory) {
        if (directory == null || !Desktop.isDesktopSupported() || !directory.exists()) {
            return;
        }
        try {
            Desktop.getDesktop().open(directory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidPassword(char[] password) {
        if (password == null) return false;

        for (char c : password) {
            if (!Character.isWhitespace(c)) return true;
        }
        return false;
    }
}
