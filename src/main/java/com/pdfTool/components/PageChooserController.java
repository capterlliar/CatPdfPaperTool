package com.pdfTool.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class PageChooserController extends GridPane {
    @FXML
    TextField tf1;
    @FXML
    TextField tf2;
    public PageChooserController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PageChooser.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public Pair<Integer, Integer> getPageRange() {
        String s1 = this.tf1.getText();
        String s2 = this.tf2.getText();
        //TODO: judge null or empty
        return new Pair<>(Integer.parseInt(s1), Integer.parseInt(s2));
    }
}
