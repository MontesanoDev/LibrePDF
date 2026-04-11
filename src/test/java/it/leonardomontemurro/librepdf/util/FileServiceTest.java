package it.leonardomontemurro.librepdf.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileServiceTest {

    @TempDir
    Path tempDir;

    // --- getUniqueFilePath ---

    @Test
    void testGetUniqueFilePathNoConflict() {
        String path = FileService.getUniqueFilePath(tempDir.toString(), "merged");
        assertTrue(path.endsWith("merged.pdf"));
        assertFalse(new File(path).exists());
    }

    @Test
    void testGetUniqueFilePathWithOneConflict() throws IOException {
        Files.createFile(tempDir.resolve("merged.pdf"));
        String path = FileService.getUniqueFilePath(tempDir.toString(), "merged");
        assertTrue(path.endsWith("merged (1).pdf"));
        assertFalse(new File(path).exists());
    }

    @Test
    void testGetUniqueFilePathWithMultipleConflicts() throws IOException {
        Files.createFile(tempDir.resolve("merged.pdf"));
        Files.createFile(tempDir.resolve("merged (1).pdf"));
        Files.createFile(tempDir.resolve("merged (2).pdf"));
        String path = FileService.getUniqueFilePath(tempDir.toString(), "merged");
        assertTrue(path.endsWith("merged (3).pdf"));
    }

    // --- createDir ---

    @Test
    void testCreateDirSuccess() throws IOException {
        File pdf = tempDir.resolve("document.pdf").toFile();
        Files.createFile(pdf.toPath());

        File dir = FileService.createDir(pdf, "document", "_images");
        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());
        assertEquals("document_images", dir.getName());
    }

    @Test
    void testCreateDirWithConflict() throws IOException {
        File pdf = tempDir.resolve("doc.pdf").toFile();
        Files.createFile(pdf.toPath());
        Files.createDirectory(tempDir.resolve("doc_images"));

        File dir = FileService.createDir(pdf, "doc", "_images");
        assertTrue(dir.exists());
        assertEquals("doc(1)_images", dir.getName());
    }

    @Test
    void testCreateDirWithMultipleConflicts() throws IOException {
        File pdf = tempDir.resolve("test.pdf").toFile();
        Files.createFile(pdf.toPath());
        Files.createDirectory(tempDir.resolve("test_images"));
        Files.createDirectory(tempDir.resolve("test(1)_images"));

        File dir = FileService.createDir(pdf, "test", "_images");
        assertTrue(dir.exists());
        assertEquals("test(2)_images", dir.getName());
    }
}
