module it.leonardomontemurro.librepdf {
    requires javafx.controls;

    requires atlantafx.base;
    requires org.apache.pdfbox;
    requires org.apache.pdfbox.io;
    requires java.desktop;

    exports it.leonardomontemurro.librepdf;
    exports it.leonardomontemurro.librepdf.ui;
    exports it.leonardomontemurro.librepdf.core;
    exports it.leonardomontemurro.librepdf.util;
}