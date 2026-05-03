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
import it.leonardomontemurro.librepdf.util.I18N;
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
    private File outputDirectory;

    public PdfToJpeg(List<File> files, int dpi)  {
        this.sources = files;
        this.dpi = dpi;
    }

    public void execute() {
        outputDirectory = sources.getFirst().getParentFile();

        for (File pdf : sources) {
            String cleanName = pdf.getName().replaceFirst("(?i)\\.pdf$", "");
            File outputDir = FileService.createDir(pdf, cleanName, "_images");

            try (PDDocument document = Loader.loadPDF(pdf)) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);

                for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {

                    BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, dpi);

                    File outputFile = new File(outputDir, "page_" + (pageIndex + 1) + ".jpg");
                    ImageIO.write(image, "jpg", outputFile);
                    image.flush();
                }

            } catch (Exception e) {
                throw new RuntimeException(I18N.get("alert.convert.jpg.error") + pdf.getName(), e);
            }
        }
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }
}
