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

import it.leonardomontemurro.librepdf.core.*;
import it.leonardomontemurro.librepdf.util.AlertService;
import it.leonardomontemurro.librepdf.util.I18N;
import javafx.application.Platform;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class PdfEngine {

    private Runnable onOperationStarted;
    private Consumer<File> onOperationCompleted;

    public void convertToJpeg(List<File> pdfs, int dpi) {
        onOperationStarted.run();
        Thread.startVirtualThread(() -> {
            File output = null;
            try {
                PdfToJpeg op = new PdfToJpeg(pdfs, dpi);
                op.execute();
                output = op.getOutputDirectory();
            } catch (Exception e) {
                AlertService.error(I18N.get("alert.convert.jpg.error") + ": " + e.getMessage());
            } finally {
                notifyCompleted(output);
            }
        });
    }

    public void protectFile(List<File> pdfs, char[] password) {
        if (isValidPassword(password)) {
            onOperationStarted.run();
            Thread.startVirtualThread(() -> {
                File output = null;
                try {
                    Protect op = new Protect(pdfs, password);
                    op.execute();
                    output = op.getOutputDirectory();
                } catch (Exception e) {
                    AlertService.error(I18N.get("alert.protect.error") + ": " + e.getMessage());
                } finally {
                    Arrays.fill(password, '\0');
                    notifyCompleted(output);
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
            onOperationStarted.run();
            Thread.startVirtualThread(() -> {
                File output = null;
                try {
                    Merge op = new Merge(pdfs);
                    op.execute();
                    output = op.getOutputDirectory();
                } catch (Exception e) {
                    AlertService.error(I18N.get("alert.merge.error") + ": " + e.getMessage());
                } finally {
                    notifyCompleted(output);
                }
            });
        } else {
            AlertService.warning(I18N.get("alert.single.file.merge.error"));
        }
    }

    public void splitFile(List<File> pdfs, List<int[]> ranges, boolean isSplitAllPagesSelected){
        Thread.startVirtualThread(() -> {
            try{
                new Split(pdfs, ranges, isSplitAllPagesSelected).execute();
            } catch (Exception e) {
                AlertService.error(I18N.get("alert.split.error") + ": " + e.getMessage());
            }
        });
    }

    public void setOnOperationStarted(Runnable callback) {
        this.onOperationStarted = callback;
    }

    public void setOnOperationCompleted(Consumer<File> callback) {
        this.onOperationCompleted = callback;
    }

    private void notifyCompleted(File outputDirectory) {
        Platform.runLater(() -> onOperationCompleted.accept(outputDirectory));
    }

    private boolean isValidPassword(char[] password) {
        if (password == null) return false;

        for (char c : password) {
            if (!Character.isWhitespace(c)) return true;
        }
        return false;
    }
}
