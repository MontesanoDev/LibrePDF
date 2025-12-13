package it.leonardomontemurro.localpdf;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class App extends Application {
    @Override
    public void start(Stage primaryStage){
        StackPane root = new StackPane();
        Scene mainScene = new Scene(root, 600 ,300);
        primaryStage.setTitle("LocalPDF");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
    public static void main(String[] args) {

        launch();
    }
}