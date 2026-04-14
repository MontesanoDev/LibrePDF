package it.leonardomontemurro.librepdf.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class ProtectTest {

    @TempDir
    Path tempDir;

    @Test
    void testProtectCreatesOutputFile() throws IOException {
        File pdf = createPdf("test.pdf");
        new Protect(List.of(pdf), "secret".toCharArray()).execute();

        boolean found;
        try (var stream = Files.list(tempDir)) {
            found = stream.anyMatch(p -> p.getFileName().toString().startsWith("protected") && p.toString().endsWith(".pdf"));
        }
        assertTrue(found, "protected.pdf file must be created");
    }

    @Test
    void testProtectOutputIsEncrypted() throws IOException {
        File pdf = createPdf("test.pdf");
        new Protect(List.of(pdf), "secret".toCharArray()).execute();

        File output;
        try (var stream = Files.list(tempDir)) {
            output = stream.filter(p -> p.getFileName().toString().startsWith("protected"))
                    .findFirst().orElseThrow().toFile();
        }

        try (PDDocument doc = Loader.loadPDF(output, "secret")) {
            assertTrue(doc.isEncrypted(), "Output file must be encrypted");
        }
    }

    @Test
    void testProtectMultiplePdfs() throws IOException {
        File pdf1 = createPdf("a.pdf");
        File pdf2 = createPdf("b.pdf");
        new Protect(List.of(pdf1, pdf2), "pass".toCharArray()).execute();

        long count;
        try (var stream = Files.list(tempDir)) {
            count = stream.filter(p -> p.getFileName().toString().startsWith("protected") && p.toString().endsWith(".pdf"))
                    .count();
        }
        assertEquals(2, count, "2 protected files must be created");
    }

    @Test
    void testProtectAvoidOverwrite() throws IOException {
        File pdf1 = createPdf("x.pdf");
        File pdf2 = createPdf("y.pdf");
        new Protect(List.of(pdf1), "pass".toCharArray()).execute();
        new Protect(List.of(pdf2), "pass".toCharArray()).execute();

        boolean hasIncrement;
        try (var stream = Files.list(tempDir)) {
            hasIncrement = stream.anyMatch(p -> p.getFileName().toString().equals("protected (1).pdf"));
        }
        assertTrue(hasIncrement, "Second file must not overwrite the first one");
    }

    @Test
    void testProtectNonPdfThrows() throws IOException {
        File txt = new File(tempDir.toFile(), "fake.txt");
        Files.write(txt.toPath(), "not a pdf".getBytes());
        assertThrows(RuntimeException.class, () -> new Protect(List.of(txt), "pass".toCharArray()).execute());
    }

    private File createPdf(String name) throws IOException {
        File f = new File(tempDir.toFile(), name);
        try (PDDocument doc = new PDDocument()) {
            doc.addPage(new PDPage());
            doc.save(f);
        }
        return f;
    }
}
