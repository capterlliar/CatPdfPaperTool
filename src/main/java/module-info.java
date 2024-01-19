module com.pdfTool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires fxPDF;

    opens com.pdfTool to javafx.fxml;
    exports com.pdfTool;
    exports com.pdfTool.utils;
    opens com.pdfTool.utils to javafx.fxml;
}