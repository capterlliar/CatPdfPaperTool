package com.pdfTool;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class PrimaryViewController extends BorderPane {
    @FXML PDFViewController pdfViewController;
    @FXML FileViewController fileViewController;
    public PrimaryViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/PrimaryView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    private void init(){
        pdfViewController.prefWidthProperty().bind(this.widthProperty().multiply(0.7));
        fileViewController.prefWidthProperty().bind(this.widthProperty().multiply(0.3));
    }
}
