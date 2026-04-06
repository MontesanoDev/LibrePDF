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

package it.leonardomontemurro.librepdf;

import it.leonardomontemurro.librepdf.core.Merge;
import it.leonardomontemurro.librepdf.core.PdfToJpeg;
import it.leonardomontemurro.librepdf.core.Protect;
import it.leonardomontemurro.librepdf.core.Unprotect;
import it.leonardomontemurro.librepdf.util.AlertService;
import it.leonardomontemurro.librepdf.util.I18N;

import java.io.File;
import java.util.List;

public class PdfEngine {

    private final String password;

    public PdfEngine(String password) {
        this.password = password;
    }

    public void run(PdfOperation currentOperation, List<File> pdfs) {
        switch (currentOperation) {
            case MERGE -> mergeFile(pdfs);
            case PROTECT -> protectFile(pdfs);
            case UNLOCK -> unprotectFile(pdfs);
            case PDFTOJPEG -> convertToJpeg(pdfs);
        }
    }

    public void convertToJpeg(List<File> pdfs) {
        Thread.startVirtualThread(() -> {
            new PdfToJpeg(pdfs, 300).execute();
        });
    }

    public void protectFile(List<File> pdfs) {
        Thread.startVirtualThread(() -> {
            new Protect(pdfs, "password").execute(); //TODO Add PasswordField to UI
        });
    }

    public void unprotectFile(List<File> pdfs) {
        Thread.startVirtualThread(() -> {
            new Unprotect(pdfs, "password").execute(); //TODO Add PasswordField to UI
        });
    }

     public void mergeFile(List<File> pdfs) {
         Thread.startVirtualThread(() -> new Merge(pdfs).execute());
    }
}
