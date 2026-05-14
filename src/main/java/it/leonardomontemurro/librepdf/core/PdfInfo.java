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
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDNameTreeNode;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PdfInfo {

    private final List<File> sources;

    public PdfInfo(List<File> files)  {
        this.sources = files;
    }

    public List<PdfInfoData> execute() {
        List<PdfInfoData> result = new ArrayList<>();
        for (File f : sources) {
            try (PDDocument doc = Loader.loadPDF(f)) {
                PDDocumentInformation meta = doc.getDocumentInformation();
                PDDocumentCatalog catalog = doc.getDocumentCatalog();

                boolean hasJs = false;
                boolean hasAttachments = false;
                if (catalog.getNames() != null) {
                    PDNameTreeNode<?> js = catalog.getNames().getJavaScript();
                    hasJs = js != null;
                    hasAttachments = catalog.getNames().getEmbeddedFiles() != null;
                }
                boolean hasForms = catalog.getAcroForm() != null
                        && !catalog.getAcroForm().getFields().isEmpty();
                boolean hasAnnotations = false;
                for (PDPage page : doc.getPages()) {
                    if (page.getAnnotations() != null && !page.getAnnotations().isEmpty()) {
                        hasAnnotations = true;
                        break;
                    }
                }

                result.add(new PdfInfoData(
                    f.getName(),
                    f.length(),
                    doc.getNumberOfPages(),
                    String.valueOf(doc.getVersion()),
                    meta.getTitle(),
                    meta.getAuthor(),
                    meta.getSubject(),
                    meta.getKeywords(),
                    meta.getCreator(),
                    meta.getProducer(),
                    meta.getCreationDate() != null ? meta.getCreationDate().getTime().toString() : null,
                    meta.getModificationDate() != null ? meta.getModificationDate().getTime().toString() : null,
                    doc.isEncrypted(),
                    hasJs,
                    hasAttachments,
                    hasForms,
                    hasAnnotations
                ));
            } catch (InvalidPasswordException e) {
                result.add(new PdfInfoData(
                    f.getName(),
                    f.length(),
                    0,
                    null,
                    null, null, null, null, null, null,
                    null, null,
                    true,
                    false, false, false, false
                ));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

}
