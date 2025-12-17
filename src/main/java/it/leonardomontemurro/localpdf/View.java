package it.leonardomontemurro.localpdf;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class View {
    private final static int HEIGHT_SIZE = 400;
    private final static int WIDTH_SIZE = 600;

    private Scene scene;

    public void setGlobalTheme(){
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
    }
    public void initializeScene(){
        StackPane root = new StackPane();
        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();
        borderPane.setCenter(gridPane);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setGridLinesVisible(true);//debugging reason

        Button button = new Button();
        button.setText("Prova");
        gridPane.add(button,0,0);
        root.getChildren().add(borderPane);
        setScene(root);
    }

    private void setScene(StackPane root){
        this.scene = new Scene(root, WIDTH_SIZE, HEIGHT_SIZE);
    }

    public Scene getScene(){
        return scene;
    }
}
