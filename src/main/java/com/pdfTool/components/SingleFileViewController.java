package com.pdfTool.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import lombok.Getter;

public class SingleFileViewController extends HBox {
    @FXML
    @Getter
    TextArea textArea;
    boolean choosed = false;

    private void init(String filename) {
        this.textArea.setText(filename);
    }
    public SingleFileViewController(String filename) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SingleFileView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init(filename);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
