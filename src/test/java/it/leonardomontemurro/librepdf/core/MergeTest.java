package it.leonardomontemurro.librepdf.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class MergeTest {

    @TempDir
    Path tempDir;

    @Test
    void testMergeTwoPdfs() throws IOException {
        File pdf1 = createPdf("a.pdf", 1);
        File pdf2 = createPdf("b.pdf", 2);

        assertDoesNotThrow(() -> new Merge(List.of(pdf1, pdf2)).execute());

        boolean found = Files.list(tempDir)
                .anyMatch(p -> p.getFileName().toString().startsWith("merged") && p.toString().endsWith(".pdf"));
        assertTrue(found, "merged.pdf file must be created");
    }

    @Test
    void testMergeSingleFile() throws IOException {
        File pdf = createPdf("solo.pdf", 1);
        assertDoesNotThrow(() -> new Merge(List.of(pdf)).execute());
    }

    @Test
    void testMergeOutputHasCorrectPageCount() throws IOException {
        File pdf1 = createPdf("p1.pdf", 2);
        File pdf2 = createPdf("p2.pdf", 3);

        new Merge(List.of(pdf1, pdf2)).execute();

        File merged = Files.list(tempDir)
                .filter(p -> p.getFileName().toString().startsWith("merged"))
                .findFirst().orElseThrow().toFile();

        try (PDDocument doc = org.apache.pdfbox.Loader.loadPDF(merged)) {
            assertEquals(5, doc.getNumberOfPages(), "Merged file must have 2+3=5 pages");
        }
    }

    @Test
    void testMergeNonPdfThrows() throws IOException {
        File txt = new File(tempDir.toFile(), "fake.txt");
        Files.write(txt.toPath(), "not a pdf".getBytes());
        File pdf = createPdf("good.pdf", 1);

        assertThrows(RuntimeException.class, () -> new Merge(List.of(txt, pdf)).execute());
    }

    @Test
    void testMergeAvoidOverwrite() throws IOException {
        File pdf1 = createPdf("x.pdf", 1);
        File pdf2 = createPdf("y.pdf", 1);

        // Prima merge crea merged.pdf, la seconda deve creare merged (1).pdf
        new Merge(List.of(pdf1, pdf2)).execute();
        new Merge(List.of(pdf1, pdf2)).execute();

        boolean hasIncrement = Files.list(tempDir)
                .anyMatch(p -> p.getFileName().toString().equals("merged (1).pdf"));
        assertTrue(hasIncrement, "Second merge must not overwrite the first one");
    }

    private File createPdf(String name, int pages) throws IOException {
        File f = new File(tempDir.toFile(), name);
        try (PDDocument doc = new PDDocument()) {
            for (int i = 0; i < pages; i++) doc.addPage(new PDPage());
            doc.save(f);
        }
        return f;
    }
}
