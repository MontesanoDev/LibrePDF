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
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.util.List;

public class Protect {

    private final List<File> sources;
    private final char[] password;
    private File outputDirectory;

    public Protect(List<File> files, char[] password)  {
        this.sources = files;
        this.password = password;
    }

    public void execute() {
        int keyLength = 256;
        String pwd = new String(password);
        outputDirectory = sources.getFirst().getParentFile();
        String outputDirectoryPath = outputDirectory.getAbsolutePath();

        for (File pdf : sources) {
            try (PDDocument doc = Loader.loadPDF(pdf)) {
                AccessPermission ap = new AccessPermission();
                ap.setCanPrint(false);
                ap.setCanExtractContent(false);
                StandardProtectionPolicy spp = new StandardProtectionPolicy(pwd, pwd, ap);
                spp.setEncryptionKeyLength(keyLength);
                doc.protect(spp);
                doc.save(FileService.getUniqueFilePath(outputDirectoryPath, "protected"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }
}
