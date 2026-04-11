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

import java.io.File;

public class FileService {

    public static String getUniqueFilePath(String directory, String prefix) {
        File file = new File(directory, prefix + ".pdf");
        int counter = 1;

        while (file.exists()) {
            file = new File(directory, prefix + " (" + counter + ").pdf");
            counter++;
        }

        return file.getAbsolutePath();
    }

    public static File createDir(File pdf, String cleanName, String prefix) {
        String parent = pdf.getParent();
        File outputDir = new File(parent, cleanName + prefix);

        int counter = 1;
        while (outputDir.exists()) {
            outputDir = new File(parent, cleanName + "(" + counter + ")" + prefix);
            counter++;
        }

        boolean created = outputDir.mkdirs();
        if (!created) {
            AlertService.error(I18N.get("alert.create.directory.error"));
            throw new RuntimeException(I18N.get("alert.create.directory.error") + outputDir.getAbsolutePath());
        }

        return outputDir;
    }
}
