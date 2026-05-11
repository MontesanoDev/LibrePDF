package it.leonardomontemurro.librepdf.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class I18NTest {

    @Test
    void testCoreKeysExist() {
        assertDoesNotThrow(() -> I18N.get("op.merge.name"));
        assertDoesNotThrow(() -> I18N.get("op.merge.desc"));
        assertDoesNotThrow(() -> I18N.get("ui.execute"));
        assertDoesNotThrow(() -> I18N.get("alert.title.error"));
        assertDoesNotThrow(() -> I18N.get("alert.title.warning"));
        assertDoesNotThrow(() -> I18N.get("alert.blank.password"));
        assertDoesNotThrow(() -> I18N.get("ui.drop.info"));
        assertDoesNotThrow(() -> I18N.get("ui.back"));
    }

    @Test
    void testAllOperationKeysExist() {
        String[] ops = {"merge", "split", "flatten", "swap", "metadata", "pdftojpeg", "protect", "unlock"};
        for (String op : ops) {
            String nameKey = "op." + op + ".name";
            String descKey = "op." + op + ".desc";
            assertDoesNotThrow(() -> I18N.get(nameKey), "Missing " + nameKey);
            assertDoesNotThrow(() -> I18N.get(descKey), "Missing " + descKey);
        }
    }

    @Test
    void testMissingKeyThrows() {
        assertThrows(MissingResourceException.class, () -> I18N.get("chiave.inesistente.xyz"));
    }

    @Test
    void testGetReturnsNonEmptyString() {
        String value = I18N.get("op.merge.name");
        assertNotNull(value);
        assertFalse(value.isBlank());
    }

    @Test
    void testAllLocalesHaveSameKeysAsEnglish() throws IOException {
        Set<String> referenceKeys = loadKeys("/i18n/messages_en.properties");
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
