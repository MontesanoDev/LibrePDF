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

public class PdfToJpegTest {

    @TempDir
    Path tempDir;

    @Test
    void testSinglePageProducesOneJpeg() throws IOException {
        File pdf = createPdf("single.pdf", 1);
        new PdfToJpeg(List.of(pdf), 72).execute();

        Path outDir = tempDir.resolve("single_images");
        assertTrue(Files.exists(outDir), "Output folder must exist");
        assertEquals(1, countJpegs(outDir), "There must be exactly 1 JPEG");
    }

    @Test
    void testMultiplePagesProduceMultipleJpegs() throws IOException {
        File pdf = createPdf("multi.pdf", 4);
        new PdfToJpeg(List.of(pdf), 72).execute();

        assertEquals(4, countJpegs(tempDir.resolve("multi_images")));
    }

    @Test
    void testJpegFilesAreNamedCorrectly() throws IOException {
        File pdf = createPdf("doc.pdf", 2);
        new PdfToJpeg(List.of(pdf), 72).execute();

        Path outDir = tempDir.resolve("doc_images");
        assertTrue(Files.exists(outDir.resolve("page_1.jpg")));
        assertTrue(Files.exists(outDir.resolve("page_2.jpg")));
    }

    @Test
    void testMultiplePdfsProduceMultipleFolders() throws IOException {
        File pdf1 = createPdf("a.pdf", 1);
        File pdf2 = createPdf("b.pdf", 1);
        new PdfToJpeg(List.of(pdf1, pdf2), 72).execute();

        assertTrue(Files.exists(tempDir.resolve("a_images")));
        assertTrue(Files.exists(tempDir.resolve("b_images")));
    }

    @Test
    void testNonPdfThrows() throws IOException {
        File txt = new File(tempDir.toFile(), "fake.txt");
        Files.write(txt.toPath(), "not a pdf".getBytes());

        assertThrows(RuntimeException.class, () -> new PdfToJpeg(List.of(txt), 72).execute());
    }

    private long countJpegs(Path dir) throws IOException {
        return Files.list(dir)
                .filter(p -> p.getFileName().toString().endsWith(".jpg"))
                .count();
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
