package com.pdfTool.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class SingleFileViewController extends HBox {
    @FXML
    TextArea textArea;
    @FXML
    CheckBox checkBox;
    @FXML HBox buttons;
    boolean choosed = false;

    private void init(String filename) {
        this.textArea.setText(filename);
        //TODO: textarea高度自适应
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
