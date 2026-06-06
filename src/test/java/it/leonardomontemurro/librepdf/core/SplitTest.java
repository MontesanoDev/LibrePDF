package it.leonardomontemurro.librepdf.core;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SplitTest {

    @TempDir
    Path tempDir;

    @Test
    void testOutputUsesSplitFileName() throws IOException {
        File source = tempDir.resolve("source.pdf").toFile();
        try (PDDocument document = new PDDocument()) {
            document.addPage(new PDPage());
            document.save(source);
        }

        new Split(List.of(source), null, true).execute();

        assertTrue(tempDir.resolve("split.pdf").toFile().exists());
    }
}
