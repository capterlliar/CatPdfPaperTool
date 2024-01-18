package com.pdfTool;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;

public class PDFViewController extends Pane {
    public PDFViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/PDFView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
