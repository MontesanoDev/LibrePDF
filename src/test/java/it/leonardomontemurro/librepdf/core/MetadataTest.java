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
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;

public class MetadataTest {

    @TempDir
    Path tempDir;

    @Test
    void testMetadataCreatesOutputFile() throws IOException {
        File pdf = createPdf("test.pdf", "OldTitle", "OldAuthor", "old");
        new Metadata(List.of(pdf), "NewTitle", "NewAuthor", "new", false).execute();

        boolean found;
        try (var stream = Files.list(tempDir)) {
            found = stream.anyMatch(p -> p.getFileName().toString().startsWith("metadata") && p.toString().endsWith(".pdf"));
        }
        assertTrue(found, "metadata.pdf file must be created");
    }

    @Test
    void testMetadataSetsFields() throws IOException {
        File pdf = createPdf("test.pdf", "OldTitle", "OldAuthor", "old");
        new Metadata(List.of(pdf), "NewTitle", "NewAuthor", "new,keywords", false).execute();

        File output = getOutput();
        try (PDDocument doc = Loader.loadPDF(output)) {
            PDDocumentInformation info = doc.getDocumentInformation();
            assertEquals("NewTitle", info.getTitle());
            assertEquals("NewAuthor", info.getAuthor());
            assertEquals("new,keywords", info.getKeywords());
        }
    }

    @Test
    void testNuclearClearsAllMetadata() throws IOException {
        File pdf = createPdf("test.pdf", "SensitiveTitle", "SensitiveAuthor", "sensitive");
        new Metadata(List.of(pdf), "", "", "", true).execute();

        File output = getOutput();
        try (PDDocument doc = Loader.loadPDF(output)) {
            PDDocumentInformation info = doc.getDocumentInformation();
            assertNull(info.getTitle());
            assertNull(info.getAuthor());
            assertNull(info.getKeywords());
            assertNull(doc.getDocumentCatalog().getMetadata());
        }
    }

    @Test
    void testNuclearThenSetFields() throws IOException {
        File pdf = createPdf("test.pdf", "OldTitle", "OldAuthor", "old");
        new Metadata(List.of(pdf), "FakeTitle", "FakeAuthor", "", true).execute();

        File output = getOutput();
        try (PDDocument doc = Loader.loadPDF(output)) {
            PDDocumentInformation info = doc.getDocumentInformation();
            assertEquals("FakeTitle", info.getTitle());
            assertEquals("FakeAuthor", info.getAuthor());
            assertNull(info.getKeywords());
        }
    }

    @Test
    void testBlankFieldsNotOverwritten() throws IOException {
        File pdf = createPdf("test.pdf", "OriginalTitle", "OriginalAuthor", "original");
        new Metadata(List.of(pdf), "", "", "", false).execute();

        File output = getOutput();
        try (PDDocument doc = Loader.loadPDF(output)) {
            PDDocumentInformation info = doc.getDocumentInformation();
            assertEquals("OriginalTitle", info.getTitle());
            assertEquals("OriginalAuthor", info.getAuthor());
            assertEquals("original", info.getKeywords());
        }
    }

    @Test
    void testMetadataAvoidOverwrite() throws IOException {
        File pdf1 = createPdf("a.pdf", "", "", "");
        File pdf2 = createPdf("b.pdf", "", "", "");
        new Metadata(List.of(pdf1), "T", "A", "K", false).execute();
        new Metadata(List.of(pdf2), "T", "A", "K", false).execute();

        boolean hasIncrement;
        try (var stream = Files.list(tempDir)) {
            hasIncrement = stream.anyMatch(p -> p.getFileName().toString().equals("metadata (1).pdf"));
        }
        assertTrue(hasIncrement, "Second file must not overwrite the first one");
    }

    @Test
    void testMetadataNonPdfThrows() throws IOException {
        File txt = new File(tempDir.toFile(), "fake.txt");
        Files.write(txt.toPath(), "not a pdf".getBytes());
        assertThrows(RuntimeException.class,
                () -> new Metadata(List.of(txt), "T", "A", "K", false).execute());
    }

    private File createPdf(String name, String title, String author, String keywords) throws IOException {
        File f = new File(tempDir.toFile(), name);
        try (PDDocument doc = new PDDocument()) {
            doc.addPage(new PDPage());
            PDDocumentInformation info = doc.getDocumentInformation();
            if (!title.isBlank()) info.setTitle(title);
            if (!author.isBlank()) info.setAuthor(author);
            if (!keywords.isBlank()) info.setKeywords(keywords);
            doc.setDocumentInformation(info);
            doc.save(f);
        }
        return f;
    }

    private File getOutput() throws IOException {
        try (var stream = Files.list(tempDir)) {
            return stream.filter(p -> p.getFileName().toString().startsWith("metadata"))
                    .findFirst().orElseThrow().toFile();
        }
    }
}
