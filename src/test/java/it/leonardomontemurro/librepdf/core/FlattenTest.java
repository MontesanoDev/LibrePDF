package it.leonardomontemurro.librepdf.core;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.cos.COSName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FlattenTest {

    @TempDir
    Path tempDir;

    @Test
    void testFlattenCreatesOutputFile() throws IOException {
        File pdf = createPdfWithForm("test.pdf", "Mario Rossi");
        boolean anyFlattened = new Flatten(List.of(pdf)).execute();

        assertTrue(anyFlattened, "Form-containing PDF should be flattened");
        assertTrue(getOutput().isPresent(), "flattened.pdf file must be created");
    }

    @Test
    void testFlattenRemovesFormFields() throws IOException {
        File pdf = createPdfWithForm("test.pdf", "Mario Rossi");
        new Flatten(List.of(pdf)).execute();

        File output = getOutput().orElseThrow();
        try (PDDocument doc = Loader.loadPDF(output)) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            assertTrue(form == null || form.getFields().isEmpty(),
                    "Form fields must be removed after flatten");
        }
    }

    @Test
    void testFlattenWithoutFormReturnsFalse() throws IOException {
        File pdf = createPdfWithoutForm("plain.pdf");
        boolean anyFlattened = new Flatten(List.of(pdf)).execute();

        assertFalse(anyFlattened, "Files without forms should not count as flattened");
        assertFalse(getOutput().isPresent(), "No output file expected when no form is flattened");
    }

    @Test
    void testFlattenWithEmptyFormReturnsFalse() throws IOException {
        File pdf = createPdfWithEmptyForm("empty-form.pdf");
        boolean anyFlattened = new Flatten(List.of(pdf)).execute();

        assertFalse(anyFlattened, "Empty form should not count as flattened");
    }

    @Test
    void testFlattenAvoidOverwrite() throws IOException {
        File pdf1 = createPdfWithForm("a.pdf", "A");
        File pdf2 = createPdfWithForm("b.pdf", "B");
        new Flatten(List.of(pdf1)).execute();
        new Flatten(List.of(pdf2)).execute();

        boolean hasIncrement;
        try (var stream = Files.list(tempDir)) {
            hasIncrement = stream.anyMatch(p -> p.getFileName().toString().equals("flattened (1).pdf"));
        }
        assertTrue(hasIncrement, "Second file must not overwrite the first one");
    }

    @Test
    void testFlattenNonPdfThrows() throws IOException {
        File txt = new File(tempDir.toFile(), "fake.txt");
        Files.write(txt.toPath(), "not a pdf".getBytes());
        assertThrows(RuntimeException.class,
                () -> new Flatten(List.of(txt)).execute());
    }

    @Test
    void testFlattenMixedFilesOnlySavesFormOnes() throws IOException {
        File withForm = createPdfWithForm("with-form.pdf", "X");
        File plain = createPdfWithoutForm("plain.pdf");
        boolean anyFlattened = new Flatten(List.of(withForm, plain)).execute();

        assertTrue(anyFlattened);
        long outputCount;
        try (var stream = Files.list(tempDir)) {
            outputCount = stream.filter(p -> p.getFileName().toString().startsWith("flattened")).count();
        }
        assertEquals(1, outputCount, "Only the form-containing PDF should produce an output");
    }

    private File createPdfWithForm(String name, String value) throws IOException {
        File f = new File(tempDir.toFile(), name);
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            PDAcroForm form = new PDAcroForm(doc);
            doc.getDocumentCatalog().setAcroForm(form);

            PDResources resources = new PDResources();
            var font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            resources.put(COSName.getPDFName("Helv"), font);
            form.setDefaultResources(resources);
            form.setDefaultAppearance("/Helv 12 Tf 0 g");
            form.setNeedAppearances(true);

            PDTextField field = new PDTextField(form);
            field.setPartialName("name");

            PDAnnotationWidget widget = field.getWidgets().get(0);
            widget.setRectangle(new PDRectangle(50, 700, 200, 20));
            widget.setPage(page);
            page.getAnnotations().add(widget);

            form.getFields().add(field);
            field.setValue(value);

            doc.save(f);
        }
        return f;
    }

    private File createPdfWithoutForm(String name) throws IOException {
        File f = new File(tempDir.toFile(), name);
        try (PDDocument doc = new PDDocument()) {
            doc.addPage(new PDPage());
            doc.save(f);
        }
        return f;
    }

    private File createPdfWithEmptyForm(String name) throws IOException {
        File f = new File(tempDir.toFile(), name);
        try (PDDocument doc = new PDDocument()) {
            doc.addPage(new PDPage());
            PDAcroForm form = new PDAcroForm(doc);
            doc.getDocumentCatalog().setAcroForm(form);
            doc.save(f);
        }
        return f;
    }

    private Optional<File> getOutput() throws IOException {
        try (var stream = Files.list(tempDir)) {
            return stream.filter(p -> p.getFileName().toString().startsWith("flattened"))
                    .map(Path::toFile).findFirst();
        }
    }
}
