package it.leonardomontemurro.localpdf;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class View {
    private final static int HEIGHT_SIZE = 720;
    private final static int WIDTH_SIZE = 1280;
    private final static int GRID_GAP = 15;
    private final static byte GRID_MAX_COLUMN = 4;
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

        root.getChildren().add(borderPane);
        setScene(root);
    }

    public void buildButtons(GridPane gridPane){
        String[] nameButtons = {
                "Test1", "Test2","Test3",
                "Test4","Test5","Test6",
                "Test7","Test8"
        };
        byte current_grid_row = 0;
        byte current_grid_coloumn = 0;
        for (String buttons: nameButtons){
            Button button = new Button();
            button.setText(buttons);
            if(current_grid_coloumn < GRID_MAX_COLUMN){
                gridPane.add(button,current_grid_coloumn,current_grid_row);
                current_grid_coloumn++;
                if(current_grid_coloumn == GRID_MAX_COLUMN){
                    current_grid_coloumn = 0;
                    current_grid_row++;
                }
            }
            addButtonToArrayList(button);
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

    public void addButtonToArrayList(Button buttonArrayList) {
        getButtonArrayList().add(buttonArrayList);
    }
}
