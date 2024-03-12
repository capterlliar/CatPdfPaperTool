module com.pdfTool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires static lombok;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires log4j;

    opens com.pdfTool to javafx.fxml;
    opens com.pdfTool.components to javafx.fxml;
    exports com.pdfTool;
    exports com.pdfTool.utils;
    exports com.pdfTool.defination;
    exports com.pdfTool.components;
    exports com.pdfTool.menuFunctions;
    opens com.pdfTool.menuFunctions to javafx.fxml;
    exports com.pdfTool.services;
    exports com.pdfTool.components.PDFViewer;
    opens com.pdfTool.components.PDFViewer to javafx.fxml;
}