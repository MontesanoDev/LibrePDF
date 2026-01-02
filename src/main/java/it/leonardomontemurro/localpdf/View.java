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
package it.leonardomontemurro.localpdf;

import atlantafx.base.theme.Dracula;
import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class View {
    private final static int HEIGHT_SIZE = 720;
    private final static int WIDTH_SIZE = 1280;
    private final static int GRID_GAP = 20;
    private final static byte GRID_MAX_COLUMN = 4;
    private final static int BUTTON_WIDTH = 240;
    private final static int BUTTON_HEIGHT = 210;

    private final ArrayList<Button> buttonArrayList = new ArrayList<Button>();
    private Scene scene;

    public void setGlobalTheme(){
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    }
    public void initializeScene(){
        AnchorPane root = new AnchorPane();
        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();

        gridPane.setHgap(GRID_GAP);
        gridPane.setVgap(GRID_GAP);
        gridPane.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(borderPane,0.0);
        AnchorPane.setBottomAnchor(borderPane,0.0);
        AnchorPane.setLeftAnchor(borderPane,0.0);
        AnchorPane.setRightAnchor(borderPane,0.0);
        borderPane.setCenter(gridPane);

        buildButtons(gridPane);
        buildIcons();
        root.getChildren().add(borderPane);
        setScene(root);
        getScene().getStylesheets().add("home.css");
    }

    public void buildButtons(GridPane gridPane){
        String[] nameButtons = {
                "Merge", "Test2","Test3",
                "Test4","Test5","Test6",
                "Test7","Test8"
        };
        byte currentGridRow = 0;
        byte currentGridColum = 0;
        for (String buttons: nameButtons){
            Button button = new Button();
            button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
            button.getStyleClass().add("homeButton");
            button.setText(buttons);
            if(currentGridColum < GRID_MAX_COLUMN){
                gridPane.add(button,currentGridColum,currentGridRow);
                currentGridColum++;
                if(currentGridColum == GRID_MAX_COLUMN){
                    currentGridColum = 0;
                    currentGridRow++;
                }
            }
            addButtonToArrayList(button);
        }
    }

    private void buildIcons(){
        String[] icons = {
            "mergeIcon","splitIcon","rotateIcon",
            "reorderIcon","metadataIcon","pdfToJpegIcon",
            "protectIcon","unlockIcon"
        };
        byte i = 0;
        for(String icon : icons) {
            Region region = new Region();
            region.getStyleClass().add(icon);
            getButtonArrayList().get(i).setContentDisplay(ContentDisplay.TOP);
            getButtonArrayList().get(i).setGraphic(region);
            Label label = new Label();
            label.setText("Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit");
            getButtonArrayList().get(i).setText(label.getText());
            i++;
        }
    }

    private void setScene(AnchorPane root){
        this.scene = new Scene(root, WIDTH_SIZE, HEIGHT_SIZE);
    }

    public Scene getScene(){
        return scene;
    }

    public ArrayList<Button> getButtonArrayList() {
        return buttonArrayList;
    }

    public void addButtonToArrayList(Button button) {
        getButtonArrayList().add(button);
    }
}
