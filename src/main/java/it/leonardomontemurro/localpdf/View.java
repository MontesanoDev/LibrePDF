package it.leonardomontemurro.localpdf;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class View {
    private final static int HEIGHT_SIZE = 600;
    private final static int WIDTH_SIZE = 600;

    private Scene scene;

    public void setGlobalTheme(){
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
    }
    public void initializeScene(){
        StackPane root = new StackPane();
        setScene(root);
    }

    private void setScene(StackPane root){
        scene = new Scene(root, HEIGHT_SIZE, WIDTH_SIZE);
    }

    public Scene getScene(){
        return scene;
    }
}
