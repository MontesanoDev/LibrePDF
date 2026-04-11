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
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

public class UnprotectTest {

    @TempDir
    Path tempDir;

    @Test
    void testUnprotectCreatesOutputFile() throws IOException {
        File enc = createEncryptedPdf("enc.pdf", "secret");
        new Unprotect(List.of(enc), "secret").execute();

        boolean found = Files.list(tempDir)
                .anyMatch(p -> p.getFileName().toString().startsWith("unlocked") && p.toString().endsWith(".pdf"));
        assertTrue(found, "unlocked.pdf file must be created");
    }

    @Test
    void testUnprotectOutputIsNotEncrypted() throws IOException {
        File enc = createEncryptedPdf("enc.pdf", "secret");
        new Unprotect(List.of(enc), "secret").execute();

        File output = Files.list(tempDir)
                .filter(p -> p.getFileName().toString().startsWith("unlocked"))
                .findFirst().orElseThrow().toFile();

        try (PDDocument doc = Loader.loadPDF(output)) {
            assertFalse(doc.isEncrypted(), "Output file must not be encrypted");
        }
    }

    @Test
    void testUnprotectNonEncryptedDoesNothing() throws IOException {
        File plain = createPlainPdf("plain.pdf");
        new Unprotect(List.of(plain), "").execute();

        boolean found = Files.list(tempDir)
                .anyMatch(p -> p.getFileName().toString().startsWith("unlocked"));
        assertFalse(found, "Should not create output if PDF is not encrypted");
    }

    @Test
    void testUnprotectWrongPasswordThrows() throws IOException {
        File enc = createEncryptedPdf("enc.pdf", "correct");
        assertThrows(RuntimeException.class,
                () -> new Unprotect(List.of(enc), "wrong").execute());
    }

    @Test
    void testUnprotectAvoidOverwrite() throws IOException {
        File enc1 = createEncryptedPdf("a.pdf", "pass");
        File enc2 = createEncryptedPdf("b.pdf", "pass");
        new Unprotect(List.of(enc1), "pass").execute();
        new Unprotect(List.of(enc2), "pass").execute();

        boolean hasIncrement = Files.list(tempDir)
                .anyMatch(p -> p.getFileName().toString().equals("unlocked (1).pdf"));
        assertTrue(hasIncrement, "Second file must not overwrite the first one");
    }

    @Test
    void testUnprotectNonPdfThrows() throws IOException {
        File txt = new File(tempDir.toFile(), "fake.txt");
        Files.write(txt.toPath(), "not a pdf".getBytes());
        assertThrows(RuntimeException.class,
                () -> new Unprotect(List.of(txt), "pass").execute());
    }

    private File createPlainPdf(String name) throws IOException {
        File f = new File(tempDir.toFile(), name);
        try (PDDocument doc = new PDDocument()) {
            doc.addPage(new PDPage());
            doc.save(f);
        }
        return f;
    }

    private File createEncryptedPdf(String name, String password) throws IOException {
        File f = new File(tempDir.toFile(), name);
        try (PDDocument doc = new PDDocument()) {
            doc.addPage(new PDPage());
            AccessPermission ap = new AccessPermission();
            StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, ap);
            spp.setEncryptionKeyLength(256);
            doc.protect(spp);
            doc.save(f);
        }
        return f;
    }
}
