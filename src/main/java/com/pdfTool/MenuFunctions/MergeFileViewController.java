package com.pdfTool.MenuFunctions;

import com.pdfTool.components.FileListController;
import com.pdfTool.utils.FileChooserUtil;
import com.pdfTool.utils.FileUtil;
import com.pdfTool.utils.PDFUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
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

    protected String getDirectory() {
        String directory = this.directory.getText();
        if(!FileUtil.checkAndCreateDir(directory)) {
            //TODO:文件夹不合法警告
        }
        if(!directory.endsWith(File.separator)) directory += File.separator;
        return directory;
    }

    protected String getFilename() {
        String filename = this.filename.getText();
        if(!FileUtil.isFileNameValid(filename)) {
            //TODO: 文件名不合法警告
            System.out.println(2);
        }
        if(!filename.endsWith(".pdf")) filename += ".pdf";
        return filename;
    }

    @FXML
    protected void merge() {
        String dest = this.getDirectory() + this.getFilename();
        List<File> oldFiles = this.fileList.getFiles();
        try {
            PDFUtil.mergeFiles(dest, oldFiles);
        } catch (IOException e) {
            //TODO: 合并失败警告
        }
        //TODO:导出中和导出成功提示
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
