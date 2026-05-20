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
import it.leonardomontemurro.librepdf.util.FileService;
import it.leonardomontemurro.librepdf.util.I18N;
import javafx.application.Platform;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class PdfEngine {

    private Runnable onOperationStarted;
    private Runnable onOperationAborted;
    private Runnable onPdfInfoCompleted;
    private Consumer<File> onOperationCompleted;
    private Consumer<List<PdfInfoData>> onPdfInfoReady;

    public void convertToJpeg(List<File> pdfs, int dpi) {
        onOperationStarted.run();
        Thread.startVirtualThread(() -> {
            try {
                PdfToJpeg op = new PdfToJpeg(pdfs, dpi);
                op.execute();
                notifyCompleted(op.getOutputDirectory());
            } catch (Exception e) {
                notifyAborted();
                AlertService.error(I18N.get("alert.convert.jpg.error") + ": " + e.getMessage());
            }
        });
    }

    public void protectFile(List<File> pdfs, char[] password, boolean canPrint, boolean canExtract) {
        if (new FileService().isValidPassword(password)) {
            onOperationStarted.run();
            Thread.startVirtualThread(() -> {
                try {
                    Protect op = new Protect(pdfs, password, canPrint, canExtract);
                    op.execute();
                    notifyCompleted(op.getOutputDirectory());
                } catch (Exception e) {
                    notifyAborted();
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
        if (new FileService().isValidPassword(password)) {
            onOperationStarted.run();
            Thread.startVirtualThread(() -> {
                try {
                    Unprotect op = new Unprotect(pdfs, password);
                    boolean anyDecrypted = op.execute();
                    if (anyDecrypted) {
                        notifyCompleted(op.getOutputDirectory());
                    } else {
                        notifyAborted();
                        AlertService.warning(I18N.get("alert.not.encrypted.pdf"));
                    }
                } catch (Exception e) {
                    notifyAborted();
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
        if(nuclear || checkOperation(title, author, keywords)) {
            onOperationStarted.run();
            Thread.startVirtualThread(() -> {
                try {
                    Metadata op = new Metadata(pdfs, title, author, keywords, nuclear);
                    op.execute();
                    notifyCompleted(op.getOutputDirectory());
                } catch (Exception e) {
                    notifyAborted();
                    AlertService.error(I18N.get("alert.metadata.error") + ": " + e.getMessage());
                }
            });
        } else {
            AlertService.warning(I18N.get("alert.blank.metadata.input"));
        }
    }

    public void mergeFile(List<File> pdfs) {
        if(pdfs.size() > 1) {
            onOperationStarted.run();
            Thread.startVirtualThread(() -> {
                try {
                    Merge op = new Merge(pdfs);
                    op.execute();
                    notifyCompleted(op.getOutputDirectory());
                } catch (Exception e) {
                    notifyAborted();
                    AlertService.error(I18N.get("alert.merge.error") + ": " + e.getMessage());
                }
            });
        } else {
            AlertService.warning(I18N.get("alert.single.file.merge.error"));
        }
    }

    public void flattenFile(List<File> pdfs) {
        onOperationStarted.run();
        Thread.startVirtualThread(() -> {
            try {
                Flatten op = new Flatten(pdfs);
                boolean anyFlattened = op.execute();
                if (anyFlattened) {
                    notifyCompleted(op.getOutputDirectory());
                } else {
                    notifyAborted();
                    AlertService.warning(I18N.get("alert.no.form.pdf"));
                }
            } catch (Exception e) {
                notifyAborted();
                AlertService.error(I18N.get("alert.flatten.error") + ": " + e.getMessage());
            }
        });
    }

    public void splitFile(List<File> pdfs, List<int[]> ranges, boolean isSplitAllPagesSelected){
        onOperationStarted.run();
        Thread.startVirtualThread(() -> {
            try{
                Split op = new Split(pdfs, ranges, isSplitAllPagesSelected);
                op.execute();
                notifyCompleted(op.getOutputDirectory());
            } catch (Exception e) {
                notifyAborted();
                AlertService.error(I18N.get("alert.split.error") + ": " + e.getMessage());
            }
        });
    }

    public void pdfInfo(List<File> pdfs) {
        onOperationStarted.run();
        Thread.startVirtualThread(() -> {
            try {
                List<PdfInfoData> result = new PdfInfo(pdfs).execute();
                Platform.runLater(() -> onPdfInfoReady.accept(result));
                notifyCompleted(null);
            } catch (Exception e) {
                notifyAborted();
                AlertService.error(I18N.get("alert.pdfinfo.error") + ": " + e.getMessage());
            } finally {
                Platform.runLater(() -> onPdfInfoCompleted.run());
            }
        });
    }

    private boolean checkOperation(String title, String author, String keywords){
        return !title.isBlank() || !author.isBlank() || !keywords.isBlank();
    }

    private void notifyAborted() {
        Platform.runLater(() -> onOperationAborted.run());
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

    public void setOnOperationAborted(Runnable callback) {
        this.onOperationAborted = callback;
    }

    public void setOnPdfInfoReady(Consumer<List<PdfInfoData>> callback) {
        this.onPdfInfoReady = callback;
    }

    public void setOnPdfInfoCompleted(Runnable onPdfInfoCompleted) {
        this.onPdfInfoCompleted = onPdfInfoCompleted;
    }
}
