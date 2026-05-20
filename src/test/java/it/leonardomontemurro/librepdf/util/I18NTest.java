package it.leonardomontemurro.librepdf.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class I18NTest {

    @Test
    void testCoreKeysExist() {
        assertNotEquals("!op.merge.name!", I18N.get("op.merge.name"));
        assertNotEquals("!op.merge.desc!", I18N.get("op.merge.desc"));
        assertNotEquals("!ui.execute!", I18N.get("ui.execute"));
        assertNotEquals("!alert.title.error!", I18N.get("alert.title.error"));
        assertNotEquals("!alert.title.warning!", I18N.get("alert.title.warning"));
        assertNotEquals("!alert.blank.password!", I18N.get("alert.blank.password"));
        assertNotEquals("!ui.drop.info!", I18N.get("ui.drop.info"));
        assertNotEquals("!ui.back!", I18N.get("ui.back"));
    }

    @Test
    void testAllOperationKeysExist() {
        String[] ops = {"merge", "split", "flatten", "pdfinfo", "metadata", "pdftojpeg", "protect", "unlock"};
        for (String op : ops) {
            String nameKey = "op." + op + ".name";
            String descKey = "op." + op + ".desc";
            assertNotEquals("!" + nameKey + "!", I18N.get(nameKey), "Missing " + nameKey);
            assertNotEquals("!" + descKey + "!", I18N.get(descKey), "Missing " + descKey);
        }
    }

    @Test
    void testMissingKeyReturn() {
        assertEquals("!chiave.inesistente.xyz!", I18N.get("chiave.inesistente.xyz"));
    }

    @Test
    void testGetReturnsNonEmptyString() {
        String value = I18N.get("op.merge.name");
        assertNotNull(value);
        assertFalse(value.isBlank());
    }

    @Test
    void testAllLocalesHaveSameKeysAsEnglish() throws IOException {
        Set<String> referenceKeys = loadKeys("/i18n/messages.properties");
        for (String locale : new String[]{"it", "fr", "de", "es"}) {
            Set<String> keys = loadKeys("/i18n/messages_" + locale + ".properties");

            Set<String> missing = new TreeSet<>(referenceKeys);
            missing.removeAll(keys);
            Set<String> extra = new TreeSet<>(keys);
            extra.removeAll(referenceKeys);

            assertTrue(missing.isEmpty(), locale + " is missing keys: " + missing);
            assertTrue(extra.isEmpty(), locale + " has extra keys: " + extra);
        }
    }

    private Set<String> loadKeys(String resource) throws IOException {
        Properties p = new Properties();
        try (var in = getClass().getResourceAsStream(resource)) {
            assertNotNull(in, "Resource not found: " + resource);
            p.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        }
        return p.stringPropertyNames();
    }
}
