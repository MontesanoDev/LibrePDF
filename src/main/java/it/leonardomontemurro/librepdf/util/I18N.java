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

package it.leonardomontemurro.librepdf.util;

import java.util.ResourceBundle;
import java.text.MessageFormat;

public class I18N {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages");

    public static String get(String key) {
        return bundle.getString(key);
    }

    public static String get(String key, Object... args) {
        return MessageFormat.format(bundle.getString(key), args);
    }
}
