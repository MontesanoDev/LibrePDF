module it.leonardomontemurro.librepdf.localpdf {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires atlantafx.base;

    opens it.leonardomontemurro.librepdf to javafx.fxml;
    exports it.leonardomontemurro.librepdf;
}