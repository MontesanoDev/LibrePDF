package it.leonardomontemurro.localpdf;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.util.ArrayList;

public class View {
    private final static int HEIGHT_SIZE = 720;
    private final static int WIDTH_SIZE = 1280;
    private final static int GRID_GAP = 25;
    private final static byte GRID_MAX_COLUMN = 4;
    private final static int BUTTON_WIDTH = 200;
    private final static int BUTTON_HEIGHT = 150;
    private final static byte ICON_PIXEL_SIZE = 36;
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
    }

    public void buildButtons(GridPane gridPane){
        String[] nameButtons = {
                "Test1", "Test2","Test3",
                "Test4","Test5","Test6",
                "Test7","Test8"
        };
        byte currentGridRow = 0;
        byte currentGridColum = 0;
        for (String buttons: nameButtons){
            Button button = new Button();
            button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
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
        String path = "M312-144q-29.7 0-50.85-21.15Q240-186.3 240-216v-480h-48v-72h192v-48h192v48h192v72h-48v479.57Q720-186 698.85-165T648-144H312Zm336-552H312v480h336v-480ZM384-288h72v-336h-72v336Zm120 0h72v-336h-72v336ZM312-696v480-480Z";
        SVGPath svgPath = new SVGPath();
        svgPath.setContent(path);
        Region region = new Region();
        region.setPrefSize(ICON_PIXEL_SIZE, ICON_PIXEL_SIZE);
        region.setMinSize(ICON_PIXEL_SIZE, ICON_PIXEL_SIZE);
        region.setMaxSize(ICON_PIXEL_SIZE, ICON_PIXEL_SIZE);
        region.setShape(svgPath);
        region.setBackground(Background.fill(Color.rgb(124, 109, 190)));
        getButtonArrayList().getFirst().setContentDisplay(ContentDisplay.TOP);
        getButtonArrayList().getFirst().setGraphic(region);

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
