package com.pdfTool.components;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class SingleFileViewController extends BorderPane {
    boolean choosed = false;
    public SingleFileViewController(String filename) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SingleFileView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            this.setCenter(new Text(filename));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
