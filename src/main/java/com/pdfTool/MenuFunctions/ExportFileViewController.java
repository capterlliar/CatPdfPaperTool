package com.pdfTool.MenuFunctions;

import com.pdfTool.components.PageChooserController;
import com.pdfTool.components.RemovableItemController;
import com.pdfTool.defination.ExportType;
import com.pdfTool.defination.Paper;
import com.pdfTool.utils.FileChooserUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class ExportFileViewController extends VBox {
    @FXML
    VBox pageChooser;
    @FXML
    TextField folderName;
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
    public void addFile(List<File> files){
        files.forEach(file -> {
            PageChooserController pageChooser = new PageChooserController(file.getName());
            RemovableItemController item = new RemovableItemController(this.pageChooser, pageChooser, file);
            pageChooser.prefWidthProperty().bind(item.widthProperty());
            this.pageChooser.getChildren().add(item);
        });
    }
    private void init(ExportType type) {
        this.exportType = type;
        this.setOnMouseClicked(e -> this.requestFocus());
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
    protected void addFile() {
        List<File> files = FileChooserUtil.getFiles(this.getScene().getWindow());
        if(files==null) return;
        files.forEach(file -> {
            PageChooserController pageChooser = new PageChooserController(file.getName());
            RemovableItemController item = new RemovableItemController(this.pageChooser, pageChooser, file);
            pageChooser.prefWidthProperty().bind(item.widthProperty().add(-50));
            this.pageChooser.getChildren().add(item);
            stage.sizeToScene();
        });
    }
    @FXML
    protected void export() {

    }

    @FXML
    protected void openDirectory() {
        File dir = FileChooserUtil.getDirectory(this.getScene().getWindow());
        if(dir==null) return;
        this.folderName.setText(dir.getPath());
    }
}
