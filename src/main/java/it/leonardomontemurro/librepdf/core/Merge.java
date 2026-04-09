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
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdfwriter.compress.CompressParameters;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Merge {

    private final List<File> sources;

    public Merge(List<File> files)  {
        this.sources = files;
    }

    public void execute() {
        try {
            PDFMergerUtility merger = new PDFMergerUtility();
            for (File pdf : sources) {
                merger.addSource(pdf);
            }

            String outputDirectory = sources.getFirst().getParent();
            String finalPath = FileService.getUniqueFilePath(outputDirectory);
            merger.setDestinationFileName(finalPath);

            merger.mergeDocuments(IOUtils.createMemoryOnlyStreamCache(), CompressParameters.NO_COMPRESSION);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
