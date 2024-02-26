package com.pdfTool.MenuFunctions;

import com.pdfTool.components.FileItemController;
import com.pdfTool.components.FileListController;
import com.pdfTool.components.RemovableItemController;
import com.pdfTool.services.ExportFileTask;
import com.pdfTool.services.MergeFileTask;
import com.pdfTool.utils.FileChooserUtil;
import com.pdfTool.utils.FileUtil;
import com.pdfTool.utils.PDFUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MergeFileViewController extends VBox {
    @FXML
    FileListController fileList;
    @FXML
    TextField directory;
    @FXML
    TextField filename;
    @FXML
    Label status;
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
    public void addFile(List<File> files) {
        if(files==null) return;
        this.fileList.addFile(files);
    }
    private void setStatus(String text, String color) {
        this.status.setText(text);
        this.status.setTextFill(Color.valueOf(color));
    }
    private void init() {
        this.setOnMouseClicked(e -> this.requestFocus());
    }
    private List<FileItemController> getFileItems() {
        return this.fileList.getFileContainer().getChildren().stream()
                .map(node -> {
                    Node node1 = ((RemovableItemController)node).getChild();
                    return ((FileItemController)node1);
                }).toList();
    }
    private List<File> getFiles() {
        return this.getFileItems().stream().map(FileItemController::getFile).toList();
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

    protected String getDirectory() {
        String directory = this.directory.getText();
        if(!FileUtil.checkAndCreateDir(directory)) {
            this.setStatus("目录不合法", "red");
            return null;
        }
        if(!directory.endsWith(File.separator)) directory += File.separator;
        return directory;
    }

    protected String getFilename() {
        String filename = this.filename.getText();
        if(!FileUtil.isFileNameValid(filename)) {
            this.setStatus("文件名不合法", "red");
            return null;
        }
        if(!filename.endsWith(".pdf")) filename += ".pdf";
        return filename;
    }

    @FXML
    protected void merge() {
        this.setStatus("", "black");
        String dir = this.getDirectory();
        String filename = this.getFilename();
        List<File> oldFiles = this.getFiles();
        if(dir == null || filename == null || oldFiles == null) return;
        if(oldFiles.isEmpty()) return;

        String dest = dir + filename;

        this.getFileItems().forEach(fileItemController -> {
            fileItemController.setFilenameColor("black");
        });

        MergeFileTask mergeFileTask = new MergeFileTask(dest, oldFiles);
        mergeFileTask.setOnSucceeded(e -> this.setStatus("合并成功", "green"));
        mergeFileTask.setOnFailed(e -> this.setStatus("合并失败", "red"));
        mergeFileTask.messageProperty().addListener((observableValue, s, t1) -> {
            this.getFileItems().forEach(fileItemController -> {
                if(fileItemController.getFile().getName().equals(t1)) {
                    fileItemController.setFilenameColor("green");
                }
            });
        });
        mergeFileTask.run();
        this.setStatus("导出中", "black");
    }

    @FXML
    protected void addFile() {
        List<File> files = FileChooserUtil.getFiles(this.getScene().getWindow());
        if(files==null) return;
        this.fileList.addFile(files);
        stage.sizeToScene();
    }

    @FXML
    protected void openDirectory() {
        File dir = FileChooserUtil.getDirectory(this.getScene().getWindow());
        if(dir==null) return;
        this.directory.setText(dir.getPath());
    }
}
