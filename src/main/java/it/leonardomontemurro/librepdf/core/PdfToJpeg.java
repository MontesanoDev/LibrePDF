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

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class PdfToJpeg {
    private final List<File> sources;
    private final int dpi;

    public PdfToJpeg(List<File> files, int dpi)  {
        this.sources = files;
        this.dpi = dpi;
    }

    public void execute() {
        for (File pdf : sources) {

            File outputDir = getFile(pdf);

            try (PDDocument document = Loader.loadPDF(pdf)) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);

                for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {

                    BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, dpi);

                    File outputFile = new File(outputDir, "page_" + (pageIndex + 1) + ".jpg");
                    ImageIO.write(image, "jpg", outputFile);
                }

            } catch (Exception e) {
                throw new RuntimeException("Errore durante l'estrazione delle immagini dal file: " + pdf.getName(), e);
            }
        }
    }

    private static File getFile(File pdf) {
        String cleanName = pdf.getName().replace(".pdf", "");
        File outputDir = new File(pdf.getParent(), cleanName + "_images");

        if (!outputDir.exists()) {
            boolean created = outputDir.mkdirs();
            if (!created) {
                throw new RuntimeException("Impossibile creare la cartella: " + outputDir.getAbsolutePath());
            }
        }
        return outputDir;
    }
}
