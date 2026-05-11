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

package it.leonardomontemurro.librepdf.core;

import it.leonardomontemurro.librepdf.util.FileService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Flatten {

    private final List<File> sources;
    private File outputDirectory;

    public Flatten(List<File> sources) {
        this.sources = sources;
    }

    public boolean execute() {
        boolean anyFlattened = false;
        outputDirectory = sources.getFirst().getParentFile();
        String outputDirectoryPath = outputDirectory.getAbsolutePath();

        for (File pdf : sources) {
            try (PDDocument doc = Loader.loadPDF(pdf)) {
                PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
                if (form != null && !form.getFields().isEmpty()) {
                    form.setNeedAppearances(false);
                    form.refreshAppearances();
                    form.flatten();
                    anyFlattened = true;
                    doc.save(FileService.getUniqueFilePath(outputDirectoryPath, "flattened"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return anyFlattened;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }
}
