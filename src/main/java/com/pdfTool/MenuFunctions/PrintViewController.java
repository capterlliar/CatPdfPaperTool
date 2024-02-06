package com.pdfTool.MenuFunctions;

import com.pdfTool.components.FileListController;
import com.pdfTool.utils.FileChooserUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class PrintViewController extends VBox {
    @FXML
    FileListController fileList;
    Stage stage;
    public void show() {
        Scene scene = new Scene(this);

        if(stage==null) {
            stage = new Stage();
            stage.initOwner(this.getScene().getWindow());
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pdfTool/images/cat.png")));
        }
        stage.setTitle("打印所选项");
        stage.setScene(scene);
        stage.show();
    }
    protected void init() {
        this.setOnMouseClicked(e -> this.requestFocus());
    }
    public PrintViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PrintView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    protected void print() {

    }

    @FXML
    protected void addFile() {
        List<File> files = FileChooserUtil.getFiles(this.getScene().getWindow());
        if(files==null) return;
        fileList.addFile(files);
        stage.sizeToScene();
    }
}
