package it.leonardomontemurro.localpdf;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class View {
    private final static int HEIGHT_SIZE = 400;
    private final static int WIDTH_SIZE = 600;
    private final static int GRID_GAP = 15;
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

        Button testButton = new Button();
        testButton.setText("I'm Test Button!");

        gridPane.getChildren().add(testButton);

        root.getChildren().add(borderPane);
        setScene(root);
    }

    private void setScene(AnchorPane root){
        this.scene = new Scene(root, WIDTH_SIZE, HEIGHT_SIZE);
    }

    public Scene getScene(){
        return scene;
    }
}
