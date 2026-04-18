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

import javafx.scene.control.Slider;
import javafx.util.StringConverter;

public class QualitySlider extends Slider {

    public QualitySlider() {
        this.setMin(0);
        this.setMax(2);
        this.setValue(1);

        this.setMajorTickUnit(1);
        this.setMinorTickCount(0);
        this.setShowTickMarks(true);

        this.setSnapToTicks(true);

        this.setMinorTickCount(0);

        this.setBlockIncrement(1);
        bindSliderTickLabel(this);
        this.setShowTickLabels(true);
        this.getStyleClass().add("slider");
    }

    private void bindSliderTickLabel(Slider slider) {
        slider.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double value) {

                if (value == 0) return "Web";
                if (value == 1) return "High";
                if (value == 2) return "Max";
                return "";
            }

            @Override
            public Double fromString(String string) {
                return switch (string) {
                    case "Web"  -> 0.0;
                    case "Max"  -> 2.0;
                    default     -> 1.0;
                };
            }
        });
    }

    public int getSelectedDPI() {
        double value = this.getValue();

        if (value == 0.0) return 72;
        if (value == 1.0) return 150;
        return 300;
    }

}
