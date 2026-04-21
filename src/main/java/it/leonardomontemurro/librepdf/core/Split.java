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
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class Split {

    private final List<File> sources;
    private final List<int[]> ranges;
    private final boolean isSplitAllPagesSelected;

    public Split(List<File> files, List<int[]> ranges, boolean isSplitAllChecked)  {
        this.sources = files;
        this.ranges = ranges;
        this.isSplitAllPagesSelected = isSplitAllChecked;
    }

    public void execute() {

        String outputDirectory = sources.getFirst().getParent();

        for (File source : sources) {

            try (PDDocument pdDocument = Loader.loadPDF(source)) {

                if(isSplitAllPagesSelected){
                    Splitter splitter = new Splitter();

                    List<PDDocument> Pages = splitter.split(pdDocument);

                    Iterator<PDDocument> iterator = Pages.listIterator();

                    while(iterator.hasNext()) {
                        try(PDDocument pd = iterator.next()) {
                            pd.save(FileService.getUniqueFilePath(outputDirectory, "splitted"));
                        }
                    }
                } else {

                    for (int[] range : ranges) {

                        int from = range[0];
                        int to = range[1];
                        int fileBound = pdDocument.getNumberOfPages();

                        if(to > fileBound){
                            to = fileBound;
                        }

                        try (PDDocument output = new PDDocument()) {

                            if (from <= to) {
                                for (int i = from; i <= to; i++) {
                                    output.importPage(pdDocument.getPage(i - 1));
                                }

                            } else {
                                for (int i = from; i >= to; i--) {
                                    output.importPage(pdDocument.getPage(i - 1));
                                }
                            }
                            output.save(
                                    FileService.getUniqueFilePath(outputDirectory, "splitted")
                            );
                        }
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
