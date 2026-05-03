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

import atlantafx.base.theme.PrimerDark;
import it.leonardomontemurro.librepdf.PdfOperation;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.function.Consumer;

public class View {

    private final static int HEIGHT_SIZE = 720;
    private final static int WIDTH_SIZE = 1280;
    private final static int GRID_GAP = 20;
    private final static byte GRID_MAX_COLUMN = 4;

    private final AnchorPane root = new AnchorPane();
    private final StackPane stackPane = new StackPane();
    private final GridPane gridPane = new GridPane();
    private final Label footerInfo = new Label();

    private Consumer<PdfOperation> onOperationSelected;

    private Scene scene;

    public void setGlobalTheme(){
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    }
    public void initializeScene(){
        buildGridPane();
        buildButtons(gridPane);
        initializeAnchorPane();
        buildStackPane();
        root.getChildren().add(stackPane);
        setScene(root);
        getScene().getStylesheets().add(getClass().getResource("/home.css").toExternalForm());
    }

    private void buildStackPane(){
        stackPane.getChildren().addAll(gridPane, footerInfo);

        StackPane.setAlignment(gridPane, Pos.CENTER);
        StackPane.setAlignment(footerInfo, Pos.BOTTOM_CENTER);

        StackPane.setMargin(footerInfo, new Insets(0, 0, 20, 0));
    }

    private void initializeAnchorPane(){
        AnchorPane.setTopAnchor(stackPane,0.0);
        AnchorPane.setBottomAnchor(stackPane,0.0);
        AnchorPane.setLeftAnchor(stackPane,0.0);
        AnchorPane.setRightAnchor(stackPane,0.0);
    }

    private void buildGridPane(){
        gridPane.setHgap(GRID_GAP);
        gridPane.setVgap(GRID_GAP);
        gridPane.setAlignment(Pos.CENTER);
    }

    void addToStackPane(Node node) {
        stackPane.getChildren().add(node);
    }

    public void buildButtons(GridPane gridPane){
        byte i = 0;
        for(PdfOperation icon : PdfOperation.values()){
            int col = i % GRID_MAX_COLUMN;
            int row = i / GRID_MAX_COLUMN;
            HomeButton button = new HomeButton(icon);
            button.setOnAction(_ -> onOperationSelected.accept(icon));
            gridPane.add(button,col,row);
            i++;
        }
    }

    StackPane getStackPane() {
        return stackPane;
    }

    void setOnOperationSelected(Consumer<PdfOperation> callback) {
        this.onOperationSelected = callback;
    }

    void setScene(AnchorPane root){
        this.scene = new Scene(root, WIDTH_SIZE, HEIGHT_SIZE);
    }

    void setHomeVisible(Boolean visible) {
        gridPane.setVisible(visible);
        gridPane.setDisable(!visible);
    }

    public Scene getScene(){
        return scene;
    }
}
