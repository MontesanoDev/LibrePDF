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
import it.leonardomontemurro.librepdf.core.Metadata;
import it.leonardomontemurro.librepdf.core.PdfToJpeg;
import it.leonardomontemurro.librepdf.core.Protect;
import it.leonardomontemurro.librepdf.core.Unprotect;
import it.leonardomontemurro.librepdf.util.AlertService;
import it.leonardomontemurro.librepdf.util.I18N;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PdfEngine {

    public void convertToJpeg(List<File> pdfs, int dpi) {
        Thread.startVirtualThread(() -> {
            try {
                new PdfToJpeg(pdfs, dpi).execute();
            } catch (Exception e) {
                AlertService.error(I18N.get("alert.convert.jpg.error") + ": " + e.getMessage());
            }
        });
    }

    public void protectFile(List<File> pdfs, char[] password) {
        if (isValidPassword(password)) {
            Thread.startVirtualThread(() -> {
                try {
                    new Protect(pdfs, password).execute();
                } catch (Exception e) {
                    AlertService.error(I18N.get("alert.protect.error") + ": " + e.getMessage());
                } finally {
                    Arrays.fill(password, '\0');
                }
            });
        } else {
            AlertService.warning(I18N.get("alert.blank.password"));
        }
    }

    public void unprotectFile(List<File> pdfs, char[] password) {
        if (isValidPassword(password)) {
            Thread.startVirtualThread(() -> {
                try {
                    boolean anyDecrypted = new Unprotect(pdfs, password).execute();
                    if (!anyDecrypted) {
                        AlertService.warning(I18N.get("alert.not.encrypted.pdf"));
                    }
                } catch (Exception e) {
                    AlertService.error(I18N.get("alert.unprotect.error") + ": " + e.getMessage());
                } finally {
                    Arrays.fill(password, '\0');
                }
            });
        } else {
            AlertService.warning(I18N.get("alert.blank.password"));
        }
    }

    public void editMetadata(List<File> pdfs, String title, String author, String keywords, boolean nuclear) {
        Thread.startVirtualThread(() -> {
            try {
                new Metadata(pdfs, title, author, keywords, nuclear).execute();
            } catch (Exception e) {
                AlertService.error(I18N.get("alert.metadata.error") + ": " + e.getMessage());
            }
        });
    }

    public void mergeFile(List<File> pdfs) {
        if(pdfs.size() > 1) {
            Thread.startVirtualThread(() -> {
                try {
                    new Merge(pdfs).execute();
                } catch (Exception e) {
                    AlertService.error(I18N.get("alert.merge.error") + ": " + e.getMessage());
                }
            });
        } else {
            AlertService.warning(I18N.get("alert.single.file.merge.error"));
        }
    }

    private boolean isValidPassword(char[] password) {
        if (password == null) return false;

        for (char c : password) {
            if (!Character.isWhitespace(c)) return true;
        }
        return false;
    }
}