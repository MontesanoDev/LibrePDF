package it.leonardomontemurro.librepdf.core;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SplitTest {

    @TempDir
    Path tempDir;

    @Test
    void testOutputUsesSplitFileName() throws IOException {
        File source = createPdf("source.pdf", 100);

        new Split(List.of(source), null, true).execute();

        assertTrue(tempDir.resolve("split.pdf").toFile().exists());
    }

    @Test
    void testSplitAllPagesCreatesOneOutputPerPage() throws IOException {
        File source = createPdf("source.pdf", 100, 200, 300);

        new Split(List.of(source), null, true).execute();

        List<Path> outputs = splitOutputs();
        assertEquals(3, outputs.size());
        for (Path output : outputs) {
            assertEquals(1, pageCount(output));
        }
    }

    @Test
    void testRangeCreatesExpectedPageCount() throws IOException {
        File source = createPdf("source.pdf", 100, 200, 300);

        new Split(List.of(source), List.of(new int[]{1, 2}), false).execute();

        assertEquals(2, pageCount(tempDir.resolve("split.pdf")));
    }

    @Test
    void testMultipleRangesCreateMultipleOutputs() throws IOException {
        File source = createPdf("source.pdf", 100, 200, 300);

        new Split(List.of(source), List.of(new int[]{1, 1}, new int[]{3, 3}), false).execute();

        List<Path> outputs = splitOutputs();
        assertEquals(2, outputs.size());
        for (Path output : outputs) {
            assertEquals(1, pageCount(output));
        }
    }

    @Test
    void testReverseRangeKeepsRequestedOrder() throws IOException {
        File source = createPdf("source.pdf", 100, 200, 300);

        new Split(List.of(source), List.of(new int[]{3, 1}), false).execute();

        try (PDDocument document = Loader.loadPDF(tempDir.resolve("split.pdf").toFile())) {
            assertEquals(300, pageWidth(document, 0), 0.01);
            assertEquals(200, pageWidth(document, 1), 0.01);
            assertEquals(100, pageWidth(document, 2), 0.01);
        }
    }

    private File createPdf(String name, float... pageWidths) throws IOException {
        File source = tempDir.resolve(name).toFile();
        try (PDDocument document = new PDDocument()) {
            for (float width : pageWidths) {
                document.addPage(new PDPage(new PDRectangle(width, 400)));
            }
            document.save(source);
        }
        return source;
    }

    private List<Path> splitOutputs() throws IOException {
        try (var stream = Files.list(tempDir)) {
            return stream
                    .filter(path -> path.getFileName().toString().startsWith("split"))
                    .filter(path -> path.getFileName().toString().endsWith(".pdf"))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .toList();
        }
    }

    private int pageCount(Path pdf) throws IOException {
        try (PDDocument document = Loader.loadPDF(pdf.toFile())) {
            return document.getNumberOfPages();
        }
    }

    private float pageWidth(PDDocument document, int pageIndex) {
        return document.getPage(pageIndex).getMediaBox().getWidth();
    }
}
