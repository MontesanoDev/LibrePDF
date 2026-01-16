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

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;


import java.util.ArrayList;

public class View {

    private final static int HEIGHT_SIZE = 720;
    private final static int WIDTH_SIZE = 1280;
    private final static int GRID_GAP = 20;
    private final static byte GRID_MAX_COLUMN = 4;
    private final static int BUTTON_WIDTH = 240;
    private final static int BUTTON_HEIGHT = 210;

    private final ArrayList<Button> buttonArrayList = new ArrayList<>();

    private final AnchorPane root = new AnchorPane();
    private final BorderPane borderPane = new BorderPane();
    private final GridPane gridPane = new GridPane();
    private final StackPane stackPane = new StackPane();
    private final Pane dragAndDropPane = new Pane();
    private final Label dragAndDropInfo = new Label();
    private final Label footer = new Label();

    private Scene scene;

    public void setGlobalTheme(){
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    }
    public void initializeScene(){
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
        buildFooter();
        BorderPane.setAlignment(footer, Pos.CENTER);
        borderPane.setBottom(footer);
        root.getChildren().add(borderPane);
        setScene(root);
        getScene().getStylesheets().add("home.css");
    }

    private void buildFooter(){
        footer.setText("Built with <3");
        footer.setAlignment(Pos.CENTER);
        footer.getStyleClass().add("footer");
        footer.setTextAlignment(TextAlignment.CENTER);
    }

    public void buildButtons(GridPane gridPane){
        byte i = 0;
        for(Icons icon : Icons.values()){
            Button button = new Button();
            button.getStyleClass().add("homeButton");
            button.setWrapText(true);
            button.setOnAction(_ -> buildAction(icon));
            button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
            int col = i % GRID_MAX_COLUMN;
            int row = i / GRID_MAX_COLUMN;
            gridPane.add(button,col,row);
            addButtonToArrayList(button);
            i++;
        }
    }

    private void buildIcons(){
        byte i = 0;
        for(Icons icon : Icons.values()) {
            Region region = new Region();
            region.getStyleClass().add(icon.getName());
            getButtonArrayList().get(i).setContentDisplay(ContentDisplay.TOP);
            getButtonArrayList().get(i).setGraphic(region);
            getButtonArrayList().get(i).setText(icon.getDescription());
            i++;
        }
    }

    private void buildAction(Icons icon){
        new Handler(icon.getName());
        buildDragAndDropScene();
    }

    private void buildDragAndDropScene() {
        setHomeVisible(false);
        dragAndDropPane.maxWidthProperty().bind(stackPane.widthProperty().multiply(0.35));
        dragAndDropPane.maxHeightProperty().bind(stackPane.heightProperty().multiply(0.55));
        dragAndDropPane.getChildren().add(setInfo());
        dragAndDropPane.getStyleClass().add("dragAndDropArea");
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(dragAndDropPane);
        borderPane.setCenter(stackPane);
    }

    private Label setInfo(){
        dragAndDropInfo.setText("Drag and drop PDF files here!");
        dragAndDropInfo.getStyleClass().add("dragAndDropInfo");
        dragAndDropInfo.maxWidthProperty().bind(dragAndDropPane.widthProperty());
        dragAndDropInfo.maxHeightProperty().bind(dragAndDropPane.heightProperty());
        dragAndDropInfo.setAlignment(Pos.CENTER);
        dragAndDropInfo.setTextAlignment(TextAlignment.CENTER);
        dragAndDropInfo.setWrapText(true);
        dragAndDropInfo.layoutXProperty().bind(dragAndDropPane.widthProperty().subtract(dragAndDropInfo.widthProperty()).divide(2));
        dragAndDropInfo.layoutYProperty().bind(dragAndDropPane.heightProperty().subtract(dragAndDropInfo.heightProperty()).divide(3));
        return dragAndDropInfo;
    }

    private void setHomeVisible(Boolean bool) {
        gridPane.setVisible(bool);
        gridPane.setManaged(bool);
        gridPane.setDisable(!bool);
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
