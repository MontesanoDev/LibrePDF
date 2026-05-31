/*
 * LibrePDF - A lightweight, native tool for manipulating PDF files.
 * Copyright (C) 2026 Leonardo Montemurro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package it.leonardomontemurro.librepdf.core;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.function.Predicate;

final class PdfCosTree {

    private PdfCosTree() {
    }

    static boolean containsJavaScript(COSBase root) {
        return walk(root, dictionary ->
                COSName.JAVA_SCRIPT.equals(dictionary.getCOSName(COSName.S))
                        || dictionary.containsKey(COSName.JS)
        );
    }

    static void removeMetadata(COSBase root) {
        walk(root, dictionary -> {
            dictionary.removeItem(COSName.METADATA);
            return false;
        });
    }

    private static boolean walk(COSBase root, Predicate<COSDictionary> visitor) {
        Deque<COSBase> pending = new ArrayDeque<>();
        Set<COSBase> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        addIfPresent(pending, root);

        while (!pending.isEmpty()) {
            COSBase current = pending.pop();
            if (!visited.add(current)) {
                continue;
            }

            switch (current) {
                case COSObject object -> addIfPresent(pending, object.getObject());
                case COSDictionary dictionary -> {
                    if (visitor.test(dictionary)) {
                        return true;
                    }
                    dictionary.getValues().forEach(value -> addIfPresent(pending, value));
                }
                case COSArray array -> array.forEach(value -> addIfPresent(pending, value));
                default -> {
                }
            }
        }

        return false;
    }

    private static void addIfPresent(Deque<COSBase> pending, COSBase value) {
        if (value != null) {
            pending.push(value);
        }
    }
}
