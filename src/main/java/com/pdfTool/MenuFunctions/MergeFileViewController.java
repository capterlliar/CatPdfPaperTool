package com.pdfTool.MenuFunctions;

import com.pdfTool.components.FileListController;
import com.pdfTool.components.RemovableItemController;
import com.pdfTool.utils.FileChooserUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class MergeFileViewController extends VBox {
    @FXML
    FileListController fileList;
    @FXML
    TextField folderName;
    Stage stage;
    public void show() {
        Scene scene = new Scene(this);

        if(stage==null) {
            stage = new Stage();
            stage.initOwner(this.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pdfTool/images/cat.png")));
        }
        stage.setTitle("合并所选项");
        stage.setScene(scene);
        stage.show();
    }
    private void init() {
        this.setOnMouseClicked(e -> this.requestFocus());
    }
    public MergeFileViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MergeFileView.fxml"));
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
    protected void merge() {

    }

    @FXML
    protected void addFile() {
        List<File> files = FileChooserUtil.getFiles(this.getScene().getWindow());
        if(files==null) return;
        fileList.addFile(files.stream().map(File::getName).toList());
        stage.sizeToScene();
    }

    @FXML
    protected void openDirectory() {
        File dir = FileChooserUtil.getDirectory(this.getScene().getWindow());
        if(dir==null) return;
        this.folderName.setText(dir.getPath());
    }
}
