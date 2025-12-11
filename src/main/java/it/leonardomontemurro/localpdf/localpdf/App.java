package it.leonardomontemurro.localpdf.localpdf;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class App extends Application {
    @Override
    public void start(Stage primaryStage){
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());

        StackPane root = new StackPane();

        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("LocalPDF");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}