/*
 *
 *  * LibrePDF - A lightweight, native tool for manipulating PDF files.
 *  * Copyright (C) 2026 Leonardo Montemurro
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package it.leonardomontemurro.librepdf;

import it.leonardomontemurro.librepdf.core.Merge;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;

public class PdfEngine {

    public void run(PdfOperation currentOperation, List<File> pdfs) {
        switch (currentOperation) {
            case MERGE -> mergeFile(pdfs);
        }
    }

     public void mergeFile(List<File> pdfs) {
         try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
             executor.submit(() -> new Merge(pdfs).execute());
         }
    }
}
