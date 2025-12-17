package it.leonardomontemurro.localpdf;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
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
        Button button = new Button();
        button.setText("Prova");
        borderPane.setCenter(button);
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
