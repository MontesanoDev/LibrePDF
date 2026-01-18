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
import javafx.event.Event;
import javafx.geometry.Insets;
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
    private final StackPane stackPane = new StackPane();
    private final GridPane gridPane = new GridPane();
    private final Pane dragAndDropPane = new Pane();
    private final Label dragAndDropInfo = new Label();
    private final Label footerInfo = new Label();
    private final Label top = new Label();
    private final Button backButton = new Button();

    private Handler handler;

    private Scene scene;

    public void setGlobalTheme(){
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    }
    public void initializeScene(){
        buildGridPane();
        buildButtons(gridPane);
        buildIcons();
        buildFooter();
        initializeAnchorPane();
        buildBackButton();
        initializeDragAndDropScene();
        setDragAndDropVisible(false);

        stackPane.getChildren().addAll(gridPane, dragAndDropPane, footerInfo, top, backButton);

        StackPane.setAlignment(gridPane, Pos.CENTER);
        StackPane.setAlignment(dragAndDropPane, Pos.CENTER);

        StackPane.setAlignment(footerInfo, Pos.BOTTOM_CENTER);
        StackPane.setMargin(footerInfo, new Insets(0, 0, 20, 0));

        StackPane.setAlignment(top, Pos.TOP_CENTER);
        StackPane.setMargin(top, new Insets(30, 0, 0, 0));

        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(20, 0, 0, 20));

        root.getChildren().add(stackPane);
        setScene(root);
        getScene().getStylesheets().add("home.css");
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

    private void buildTop(){
        top.setText(handler.getCurrentOperation().getName().toUpperCase());
        top.getStyleClass().add("top-label");
        top.setAlignment(Pos.CENTER);
        top.setMaxWidth(Double.MAX_VALUE);
        top.setTextAlignment(TextAlignment.CENTER);
    }

    private void buildFooter(){
        footerInfo.setText("Built with <3");
        footerInfo.setAlignment(Pos.CENTER);
        footerInfo.getStyleClass().add("footer");
        footerInfo.setTextAlignment(TextAlignment.CENTER);
    }

    public void buildButtons(GridPane gridPane){
        byte i = 0;
        for(Icons icon : Icons.values()){
            Button button = new Button();
            button.getStyleClass().add("homeButton");
            button.setWrapText(true);
            button.setOnAction(_ -> {
                handler = new Handler(icon,this);
                handler.buildAction();
            });
            button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
            int col = i % GRID_MAX_COLUMN;
            int row = i / GRID_MAX_COLUMN;
            gridPane.add(button,col,row);
            addButtonToArrayList(button);
            i++;
        }
    }

    private void buildBackButton() {
        backButton.setText("← Back");
        backButton.getStyleClass().add("backButton");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(_-> backToHome());
    }

    private void backToHome(){
        setHomeVisible(true);
        setDragAndDropVisible(false);
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

    private void buildDragAndDropScene(){
        buildTop();
        setHomeVisible(false);
        setDragAndDropVisible(true);
    }

    public void dragAndDrop(){
        buildDragAndDropScene();
        stackPane.setOnDragEntered(entered -> {
            dragAndDropPane.getStyleClass().add("dragOver");
            entered.consume();
        });
        stackPane.setOnDragExited(exited -> {
            dragAndDropPane.getStyleClass().remove("dragOver");
            exited.consume();
        });
    }

    private void initializeDragAndDropScene() {
        dragAndDropPane.maxWidthProperty().bind(stackPane.widthProperty().multiply(0.35));
        dragAndDropPane.maxHeightProperty().bind(stackPane.heightProperty().multiply(0.55));
        dragAndDropPane.getChildren().add(setInfo());
        dragAndDropPane.getStyleClass().add("dragAndDropArea");
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

    private void setDragAndDropVisible(Boolean bool){
        dragAndDropPane.setVisible(bool);
        top.setVisible(bool);
        backButton.setVisible(bool);
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
