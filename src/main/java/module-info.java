module com.pdfTool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires fxPDF;
    requires static lombok;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;

    opens com.pdfTool to javafx.fxml;
    opens com.pdfTool.components to javafx.fxml;
    exports com.pdfTool;
    exports com.pdfTool.utils;
    exports com.pdfTool.defination;
    exports com.pdfTool.components;
}