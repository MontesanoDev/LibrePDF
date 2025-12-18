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
        testButton.setText("Test Button 1");
        Button testButton1 = new Button();
        testButton1.setText("Test Button 2");
        Button testButton2 = new Button();
        testButton2.setText("Test Button 3");
        Button testButton3 = new Button();
        testButton3.setText("Test Button 4");

        Button testButton4 = new Button();
        testButton4.setText("Test Button 5");
        Button testButton5 = new Button();
        testButton5.setText("Test Button 6");
        Button testButton6 = new Button();
        testButton6.setText("Test Button 7");
        Button testButton7 = new Button();
        testButton7.setText("Test Button 8");

        gridPane.add(testButton,0,0);
        gridPane.add(testButton1,1,0);
        gridPane.add(testButton2,2,0);
        gridPane.add(testButton3,3,0);

        gridPane.add(testButton4,0,1);
        gridPane.add(testButton5,1,1);
        gridPane.add(testButton6,2,1);
        gridPane.add(testButton7,3,1);


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
