package it.leonardomontemurro.localpdf;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class View {
    private final static int HEIGHT_SIZE = 720;
    private final static int WIDTH_SIZE = 1280;
    private final static int GRID_GAP = 20;
    private final static byte GRID_MAX_COLUMN = 4;
    private final static int BUTTON_WIDTH = 240;
    private final static int BUTTON_HEIGHT = 210;
    private final static byte ICON_PIXEL_SIZE = 56;
    private final ArrayList<Button> buttonArrayList = new ArrayList<Button>();
    private Scene scene;

    public void setGlobalTheme(){
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
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
        resizeIcons();
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

    private void resizeIcons(){
        Region region = new Region();
        region.setPrefSize(ICON_PIXEL_SIZE-5, ICON_PIXEL_SIZE);
        region.setMinSize(ICON_PIXEL_SIZE-5, ICON_PIXEL_SIZE);
        region.setMaxSize(ICON_PIXEL_SIZE-5, ICON_PIXEL_SIZE);
        region.setBackground(Background.fill(Color.WHITE));
        region.getStyleClass().add("iconButton");
        getButtonArrayList().getFirst().setContentDisplay(ContentDisplay.TOP);
        getButtonArrayList().getFirst().setGraphic(region);
        Label label = new Label();
        label.setText("Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit");
        getButtonArrayList().getFirst().setText(label.getText());
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
