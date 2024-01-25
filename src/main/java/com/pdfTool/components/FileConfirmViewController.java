package com.pdfTool.components;

import com.pdfTool.defination.ExportType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FileConfirmViewController extends VBox {
    ExportType exportType;
    Stage stage;
    public void show() {
        Scene scene = new Scene(this);

        if(stage==null) {
            stage = new Stage();
            stage.initOwner(this.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pdfTool/images/cat.png")));
        }
        stage.setTitle(this.exportType.getWindowName());
        stage.setScene(scene);
        stage.show();
    }
    private void init(ExportType type) {
        this.exportType = type;

        this.setOnMouseClicked(e -> this.requestFocus());
    }
    public FileConfirmViewController(ExportType type) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FileConfirmView.fxml"));
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
    protected void merge() {

    }
}
