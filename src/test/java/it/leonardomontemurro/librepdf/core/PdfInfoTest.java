package it.leonardomontemurro.librepdf.core;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class PdfInfoTest {

    @Test
    void testPdfHasJavascript() throws URISyntaxException {
        File pdf = new File(Objects.requireNonNull(getClass().getResource("/pdfs/has_javascript.pdf")).toURI());
        List<PdfInfoData> result = new PdfInfo(List.of(pdf)).execute();

        assertEquals(1, result.size());
        assertTrue(result.getFirst().hasJavaScript());
    }

    @Test
    void testPdfMetadata() throws URISyntaxException {
        File pdf = new File(Objects.requireNonNull(getClass().getResource("/pdfs/with_metadata.pdf")).toURI());

        PdfInfoData pdfMetadata = new PdfInfo(List.of(pdf)).execute().getFirst();

        assertAll(
                () -> assertEquals("LibrePDF Test Document", pdfMetadata.title()),
                () -> assertEquals("Montesano", pdfMetadata.author()),
                () -> assertEquals("Test fixture for PdfInfo metadata extraction", pdfMetadata.subject()),
                () -> assertEquals("test, librepdf, metadata", pdfMetadata.keywords()),
                () -> assertEquals("Asdobe", pdfMetadata.creator()),
                () -> assertEquals("bigPdf", pdfMetadata.producer()),
                () -> assertNotNull(pdfMetadata.creationDate()),
                () -> assertNotNull(pdfMetadata.modificationDate())
        );
    }

    @Test
    void testPdfIsEncrypted() throws URISyntaxException {
        File pdf = new File(Objects.requireNonNull(getClass().getResource("/pdfs/encrypted.pdf")).toURI());
        PdfInfoData pdfMetadata = new PdfInfo(List.of(pdf)).execute().getFirst();
        assertTrue(pdfMetadata.encrypted());
    }

    @Test
    void testPdfHasAttachments() throws URISyntaxException {
        File pdf = new File(Objects.requireNonNull(getClass().getResource("/pdfs/with_attachment.pdf")).toURI());
        PdfInfoData pdfAttachment = new PdfInfo(List.of(pdf)).execute().getFirst();
        assertTrue(pdfAttachment.hasAttachments());
    }
}
