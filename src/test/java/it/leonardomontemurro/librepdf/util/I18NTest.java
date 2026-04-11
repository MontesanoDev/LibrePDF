package it.leonardomontemurro.librepdf.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.MissingResourceException;

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
        String[] ops = {"merge", "split", "rotate", "swap", "metadata", "pdftojpeg", "protect", "unlock"};
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
}
