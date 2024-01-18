module com.pdfTool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens com.pdfTool to javafx.fxml;
    exports com.pdfTool;
}