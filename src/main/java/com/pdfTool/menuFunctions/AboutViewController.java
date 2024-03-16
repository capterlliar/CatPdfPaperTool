package com.pdfTool.menuFunctions;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public class AboutViewController extends VBox {
    @FXML
    Hyperlink link;
    Desktop desktop = null;
    Stage stage;
    public AboutViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AboutView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    private void init() {
        this.desktop = Desktop.getDesktop();
        this.link.setOnAction(e -> {
            try {
                this.desktop.browse(URI.create(this.link.getText()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    public void show() {
        Scene scene = new Scene(this);

        if(stage==null) {
            stage = new Stage();
            stage.initOwner(this.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(
                    Objects.requireNonNull(
                            getClass().getResourceAsStream("/com/pdfTool/images/cat.png"))));
        }
        stage.setTitle("关于");
        stage.setScene(scene);
        stage.show();
    }
}
