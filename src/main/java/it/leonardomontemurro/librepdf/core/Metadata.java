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
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.io.File;
import java.util.List;

public class Metadata {

    private final List<File> sources;
    private final String title;
    private final String author;
    private final String keywords;
    private final boolean nuclear;
    private File outputDirectory;

    public Metadata(List<File> sources, String title, String author, String keywords, boolean nuclear) {
        this.sources = sources;
        this.title = title;
        this.author = author;
        this.keywords = keywords;
        this.nuclear = nuclear;
    }

    public void execute() {
        outputDirectory = sources.getFirst().getParentFile();
        String outputDirectoryPath = outputDirectory.getAbsolutePath();

        for (File pdf : sources) {
            try (PDDocument doc = Loader.loadPDF(pdf)) {
                PDDocumentInformation info = nuclear ? new PDDocumentInformation() : doc.getDocumentInformation();

                if (nuclear) {
                    doc.getDocumentCatalog().setMetadata(null);
                }

                if (!title.isBlank()) info.setTitle(title);
                if (!author.isBlank()) info.setAuthor(author);
                if (!keywords.isBlank()) info.setKeywords(keywords);

                doc.setDocumentInformation(info);
                doc.save(FileService.getUniqueFilePath(outputDirectoryPath, "metadata"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }
}
