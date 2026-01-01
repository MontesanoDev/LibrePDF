package it.leonardomontemurro.localpdf;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.util.ArrayList;

public class View {
    private final static int HEIGHT_SIZE = 720;
    private final static int WIDTH_SIZE = 1280;
    private final static int GRID_GAP = 25;
    private final static byte GRID_MAX_COLUMN = 4;
    private final static int BUTTON_WIDTH = 220;
    private final static int BUTTON_HEIGHT = 190;
    private final static byte ICON_PIXEL_SIZE = 46;
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
                "Merge", "Test2","Test3",
                "Test4","Test5","Test6",
                "Test7","Test8"
        };
        byte currentGridRow = 0;
        byte currentGridColum = 0;
        for (String buttons: nameButtons){
            Button button = new Button();
            button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
            button.setStyle("-fx-background-radius: 10px");
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
        String path = "m256-120-56-56 193-194q23-23 35-52t12-61v-204l-64 63-56-56 160-160 160 160-56 56-64-63v204q0 32 12 61t35 52l193 194-56 56-224-224-224 224Z";
        SVGPath svgPath = new SVGPath();
        svgPath.setContent(path);
        Region region = new Region();
        region.setPrefSize(ICON_PIXEL_SIZE-5, ICON_PIXEL_SIZE);
        region.setMinSize(ICON_PIXEL_SIZE-5, ICON_PIXEL_SIZE);
        region.setMaxSize(ICON_PIXEL_SIZE-5, ICON_PIXEL_SIZE);
        region.setShape(svgPath);
        region.setBackground(Background.fill(Color.WHITE));
        getButtonArrayList().getFirst().setContentDisplay(ContentDisplay.TOP);
        getButtonArrayList().getFirst().setGraphic(region);
        Label label = new Label();
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(30,10,10,10));
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
