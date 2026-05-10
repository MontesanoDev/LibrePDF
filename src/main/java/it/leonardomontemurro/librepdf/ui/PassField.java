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
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class PassField {

    private final VBox passwordField = new VBox(15);
    private final HBox canPrintableBox = new HBox(20);
    private final HBox canExtractableBox = new HBox(20);
    private final CheckBox canPrintable = new CheckBox();
    private final CheckBox canExtractable = new CheckBox();
    private final Label canPrintableLabel = new Label();
    private final Label canExtractableLabel = new Label();
    private final PasswordField password;
    private final PasswordField confirmPassword;

    public PassField() {
        this.password = new PasswordField();
        this.confirmPassword = new PasswordField();

        buildPasswordInputFields();
        buildCheckBoxes();
    }

    private void buildCheckBoxes() {
        canPrintableBox.setPadding(new Insets(0,10,0,0));
        canExtractableBox.setPadding(new Insets(0,10,0,0));

        canPrintableLabel.setText(I18N.get("pass.can.print"));
        canPrintableLabel.setTextAlignment(TextAlignment.CENTER);
        canPrintableBox.getChildren().addAll(canPrintableLabel, canPrintable);
        canPrintableBox.setAlignment(Pos.CENTER_RIGHT);

        canExtractableLabel.setText(I18N.get("pass.can.extract"));
        canExtractableLabel.setTextAlignment(TextAlignment.CENTER);
        canExtractableBox.getChildren().addAll(canExtractableLabel, canExtractable);
        canExtractableBox.setAlignment(Pos.CENTER_RIGHT);

        passwordField.getChildren().addAll(canPrintableBox, canExtractableBox);
    }

    private void buildPasswordInputFields() {
        password.setPromptText(I18N.get("ui.password.prompt"));
        password.setAlignment(Pos.CENTER);
        confirmPassword.setPromptText(I18N.get("ui.password.confirm"));
        confirmPassword.setAlignment(Pos.CENTER);
        confirmPassword.managedProperty().bind(confirmPassword.visibleProperty());
        passwordField.getChildren().addAll(password, confirmPassword);
        passwordField.setPadding(new Insets(0,20,0,20));
        passwordField.setVisible(false);
        passwordField.managedProperty().bind(passwordField.visibleProperty());
    }

    void setPasswordFieldVisible(Boolean visible) {
        passwordField.setVisible(visible);
    }

    void setUnlockFieldVisible(Boolean visible){
        confirmPassword.setVisible(visible);
    }

    void clearPassword() {
        password.setText("");
        confirmPassword.setText("");
    }

    ObservableValue<? extends Boolean> bindBlankPassword() {
        return Bindings.createBooleanBinding(() -> {
            String t1 = password.getText();
            String t2 = confirmPassword.getText();
            boolean isPasswordEmpty = t1 == null || t1.isEmpty();
            boolean isConfirmPasswordEmpty = t2 == null || t2.isEmpty();

            if (!passwordField.isVisible()) {
                return false;
            }

            if (!confirmPassword.isVisible()) {
                return isPasswordEmpty;
            }

            return isPasswordEmpty || isConfirmPasswordEmpty || !t1.equals(t2);
        },
        password.textProperty(),
        confirmPassword.textProperty(),
        confirmPassword.visibleProperty(),
        passwordField.visibleProperty()
        );
    }

    VBox getPasswordField() {
        return passwordField;
    }

    boolean canPrintable() {
        return !canPrintable.isSelected();
    }

    boolean canExtract() {
        return !canExtractable.isSelected();
    }

    public char[] getPassword() {
        return password.getText().toCharArray();
    }
}
