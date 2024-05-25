package com.pdfTool.menuFunctions;

import com.pdfTool.components.FileItem;
import com.pdfTool.components.FileList;
import com.pdfTool.components.RemovableItem;
import com.pdfTool.services.MergeFileTask;
import com.pdfTool.utils.FileChooserUtil;
import com.pdfTool.utils.FileUtil;
import com.pdfTool.utils.TimeUtil;
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
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.util.List;
import java.util.Objects;

@Log4j
public class MergeFileViewController extends VBox {
    @FXML
    FileList fileList;
    @FXML
    TextField directory;
    @FXML
    TextField filename;
    @FXML
    Label status;
    Stage stage;
    MergeFileTask mergeFileTask;
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
    private void init() {
        this.setOnMouseClicked(e -> this.requestFocus());
    }
    public void show() {
        Scene scene = new Scene(this);

        if(stage==null) {
            stage = new Stage();
            stage.initOwner(this.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/pdfTool/images/cat.png"))));
        }
        stage.setOnCloseRequest(e -> {
            if(this.mergeFileTask == null) return;
            if(this.mergeFileTask.isRunning()) this.mergeFileTask.cancel();
        });
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
    private List<FileItem> getFileItems() {
        return this.fileList.getFileContainer().getChildren().stream()
                .map(node -> {
                    Node node1 = ((RemovableItem)node).getChild();
                    return ((FileItem)node1);
                }).toList();
    }
    private List<File> getFiles() {
        return this.getFileItems().stream().map(FileItem::getFile).toList();
    }
    private String getDirectory() {
        String directory = this.directory.getText();
        if(!FileUtil.checkAndCreateDir(directory)) {
            this.setStatus("目录不合法", "red");
            return null;
        }
        if(!directory.endsWith(File.separator)) directory += File.separator;
        return directory;
    }

    private String getFilename() {
        String filename = this.filename.getText();
        if(!filename.endsWith(".pdf")) filename += ".pdf";
        if(!FileUtil.isFileNameValid(filename)) {
            this.setStatus("文件名不合法", "red");
            return null;
        }
        return filename;
    }

    @FXML
    protected void merge() {
        String dir = this.getDirectory();
        String filename = this.getFilename();
        List<File> oldFiles = this.getFiles();
        if(dir == null || filename == null || oldFiles == null) return;
        if(oldFiles.isEmpty()) return;

        String dest = dir + filename;
        File file = new File(dest);
        if(file.exists()) {
            this.setStatus("文件已存在", "red");
            return;
        }

        this.getFileItems().forEach(fileItem -> fileItem.setFilenameColor("black"));

        mergeFileTask = new MergeFileTask(dest, oldFiles);
        mergeFileTask.setOnSucceeded(e -> {
            this.setStatus("合并成功", "green");
            TimeUtil.end();
        });
        mergeFileTask.setOnFailed(e -> {
            this.setStatus("合并失败", "red");
            Throwable exc = mergeFileTask.getException();
            log.error(exc);
            exc.printStackTrace();
        });
        mergeFileTask.setOnRunning(e -> {
            this.setStatus("导出中", "black");
            TimeUtil.end();
        });
        TimeUtil.start();
        mergeFileTask.messageProperty().addListener((observableValue, s, t1) ->
                this.getFileItems().forEach(fileItem -> {
                    if(fileItem.getFile().getName().equals(t1)) {
                        fileItem.setFilenameColor("green");
                    }
        }));
        Thread thread = new Thread(mergeFileTask);
        thread.start();
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
