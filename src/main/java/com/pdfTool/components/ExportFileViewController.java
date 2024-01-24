package com.pdfTool.components;

import com.pdfTool.defination.ExportType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class ExportFileViewController extends VBox {
    @FXML VBox pageChooser;
    ExportType exportType;
    Stage stage;
    public void show() {
        Scene scene = new Scene(this);

        stage = new Stage();
        stage.initOwner(this.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(this.exportType.getWindowName());
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pdfTool/images/cat.png")));
        stage.setScene(scene);
        stage.show();
    }
    private void init(ExportType type) {
        this.exportType = type;

        this.setOnMouseClicked(e -> {
            this.requestFocus();
        });
    }
    public ExportFileViewController(ExportType type) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ExportFileView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init(type);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    protected void addPage() {
        this.pageChooser.getChildren().add(new PageChooserController());
        stage.sizeToScene();
    }

    @FXML
    protected void export() {

    }
}
