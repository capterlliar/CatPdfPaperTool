package com.pdfTool.components;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.io.File;
import java.util.List;

public class FileList extends VBox {
    @FXML
    @Getter
    VBox fileContainer;
    @FXML
    Label cnt;
    int fileNum;
    public FileList() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FileList.fxml"));
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
        this.fileNum = 0;
        this.fileContainer.getChildren().addListener((ListChangeListener<Node>) change -> {
            while(change.next()) {
                if (change.wasAdded()) {
                    this.fileNum++;
                    this.cnt.setText("共" + this.fileNum + "个文件");
                } else if (change.wasRemoved()) {
                    this.fileNum--;
                    this.cnt.setText("共" + this.fileNum + "个文件");
                }
            }
        });
    }
    public void addFile(List<File> files) {
        files.forEach(file -> {
            FileItem fileItem = new FileItem(file);
            fileItem.prefWidthProperty().bind(this.fileContainer.widthProperty().add(-50));
            RemovableItem item = new RemovableItem(this.fileContainer, fileItem);
            this.fileContainer.getChildren().add(item);
        });
    }
}
