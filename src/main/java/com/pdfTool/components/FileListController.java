package com.pdfTool.components;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;

public class FileListController extends VBox {
    @FXML
    VBox fileContainer;
    @FXML
    Label cnt;
    int fileNum;
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
    public FileListController() {
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

    public void addFile(List<File> files) {
        files.forEach(file -> {
            Label label = new Label(file.getName());
            label.setWrapText(true);
            label.prefWidthProperty().bind(this.fileContainer.widthProperty().add(-50));
            label.setStyle("-fx-text-overrun: ellipsis;");
            RemovableItemController item = new RemovableItemController(this.fileContainer, label, file);
            this.fileContainer.getChildren().add(item);
        });
    }

    public List<File> getFiles() {
        return this.fileContainer.getChildren().stream()
                .map(node -> ((RemovableItemController)node).getFile()).toList();
    }
}
