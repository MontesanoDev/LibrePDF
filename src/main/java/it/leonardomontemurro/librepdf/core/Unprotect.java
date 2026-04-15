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

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Unprotect {

    private final List<File> sources;
    private final char[] password;

    public Unprotect(List<File> files, char[] password)  {
        this.sources = files;
        this.password = password;
    }

    public boolean execute() {
        String pwd = new String(password);
        boolean anyEncrypted = false;
        for (File pdf : sources) {

            try(PDDocument doc = Loader.loadPDF(pdf, pwd)){

                if (doc.isEncrypted()) {
                    anyEncrypted = true;
                    doc.setAllSecurityToBeRemoved(true);
                    String outputDirectory = sources.getFirst().getParent();
                    doc.save(FileService.getUniqueFilePath(outputDirectory, "unlocked"));
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return anyEncrypted;
    }
}