package it.leonardomontemurro.localpdf;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage){
        View view = new View();
        view.setGlobalTheme();
        view.initializeScene();
        primaryStage.setTitle("LibrePDF");
        primaryStage.setScene(view.getScene());
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}