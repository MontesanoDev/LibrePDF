package it.leonardomontemurro.librepdf.util;

import javafx.scene.Scene;
import javafx.scene.input.*;

public class InputSC {
    private final Scene scene;
    private Runnable onBackAction;
    private Runnable onFrontMousePressed;

    public InputSC(Scene scene) {
        this.scene = scene;
        bindSC();
    }

    private void bindSC() {
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if(event.getButton() == MouseButton.BACK && onBackAction != null) {
                onBackAction.run();
                event.consume();
            } else if(event.getButton() == MouseButton.FORWARD && onFrontMousePressed != null) {
                onFrontMousePressed.run();
                event.consume();
            }
        });
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == KeyCode.ESCAPE && onBackAction != null) {
                onBackAction.run();
                event.consume();
            }
        });
    }

    public void setOnFrontMousePressed(Runnable onFrontMousePressed) {
        this.onFrontMousePressed = onFrontMousePressed;
    }

    public void setOnBackAction(Runnable onBackAction) {
        this.onBackAction = onBackAction;
    }
}
