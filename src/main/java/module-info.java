module it.leonardomontemurro.localpdf.localpdf {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires atlantafx.base;

    opens it.leonardomontemurro.localpdf to javafx.fxml;
    exports it.leonardomontemurro.localpdf;
}