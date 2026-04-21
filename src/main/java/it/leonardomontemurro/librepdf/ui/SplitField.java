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

package it.leonardomontemurro.librepdf.ui;

import it.leonardomontemurro.librepdf.util.I18N;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.ArrayList;
import java.util.List;

public class SplitField extends TextField{

    private final List<int[]> ranges = new ArrayList<>();

    public SplitField() {

        this.setAlignment(Pos.CENTER);
        this.setPromptText(I18N.get("split.range.info"));
        bindIntegersToTextField();
    }

    private void bindIntegersToTextField() {
        TextFormatter<String> rangeFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.matches("[\\d\\s,-]*")) {
                return change;
            }
            return null;
        });

        this.setTextFormatter(rangeFormatter);
    }

    boolean isRangeValid() {
        String input = this.getText().replaceAll("\\s+", "");

        if (!input.matches("\\d+-\\d+(,\\d+-\\d+)*")) {
            return false;
        }

        String[] groups = input.split(",");

        for (String group : groups) {
            String[] parts = group.split("-");

            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);

            if(start < 1 ) {
                return false;
            }

            ranges.add(new int[]{start, end});

        }

        return !ranges.isEmpty();
    }

    public List<int[]> getRanges() {
        isRangeValid();
        return ranges;
    }
}
