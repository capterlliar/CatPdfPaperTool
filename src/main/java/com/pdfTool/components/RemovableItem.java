package com.pdfTool.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class RemovableItem extends GridPane {
    VBox parent = null;
    @Getter
    Node child = null;
    public RemovableItem(VBox parent, Node child) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RemovableItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init(parent, child);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    private void init(VBox parent, Node child) {
        this.parent = parent;
        this.child = child;
        this.add(child,1,0);
    }

    @FXML
    protected void remove() {
        this.parent.getChildren().remove(this);
        this.parent.requestFocus();
    }
}
