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
        label.setPrefWidth(300);
        label.setStyle("-fx-text-overrun: ellipsis;");
        this.add(label, 1,0);
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
