package com.pdfTool.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class PageChooserController extends GridPane {
    @FXML
    TextField textField;
    private void init(String fileName) {
        Label label = new Label(fileName);
        label.setStyle("-fx-text-overrun: ellipsis;");
        label.prefWidthProperty().bind(this.widthProperty().add(-100));
        GridPane.isFillWidth(label);
        this.add(label, 0,0);
    }
    public PageChooserController(String fileName) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PageChooser.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init(fileName);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

//    public Pair<Integer, Integer> getPageRange() {}
}
