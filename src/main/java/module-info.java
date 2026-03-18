module it.leonardomontemurro.librepdf.localpdf {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires atlantafx.base;
    requires org.apache.pdfbox;
    requires org.apache.pdfbox.io;
    requires java.xml;
    requires java.desktop;

    opens it.leonardomontemurro.librepdf to javafx.fxml;
    exports it.leonardomontemurro.librepdf;
    exports it.leonardomontemurro.librepdf.ui;
    opens it.leonardomontemurro.librepdf.ui to javafx.fxml;
    exports it.leonardomontemurro.librepdf.core;
    opens it.leonardomontemurro.librepdf.core to javafx.fxml;
}