package com.pdfTool;

import com.pdfTool.components.FilenameEditorController;
import com.pdfTool.defination.RenameItem;
import com.pdfTool.services.GetNameOptionsTask;
import com.pdfTool.services.RenameTask;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.File;
import java.util.HashSet;
import java.util.List;

/*
    FileView uses TreeView to display all loaded papers.
    The new name options will be displayed as children of the original paper item.
 */

public class FileViewController extends BorderPane {
    // Not common singleton pattern. We can guarantee that the class only be loaded once.
    // Just provide a way to access the created instance in MenuView.
    private static FileViewController INSTANCE = null;
    public static FileViewController getInstance() {
        return INSTANCE;
    }
    @FXML
    TreeView<HBox> treeView;
    @FXML
    CheckBox checkBox;
    @FXML
    Label status;
    TreeItem<HBox> rootNode = null;
    HashSet<File> files;
    Integer cnt=0;

    public void loadPaper(List<RenameItem> renameItems) {
        for(RenameItem renameItem : renameItems){
            // Prevent duplicated paper.
            if(files.contains(renameItem.getFile()))
                continue;
            files.add(renameItem.getFile());

            renameItem.setId(cnt++);
            FilenameEditorController content = new FilenameEditorController(renameItem, this);
            rootNode.getChildren().add(content);
        }
    }

    public void focusOn(TreeItem<HBox> treeItem) {
        int i = this.treeView.getRow(treeItem);
        this.treeView.scrollTo(i);
        this.treeView.getSelectionModel().select(treeItem);
    }

    private void init() {
        INSTANCE = this;
        // To initialize treeView.
        if(rootNode==null) {
            files = new HashSet<>();

            HBox space = new HBox();
            HBox.setHgrow(space, Priority.ALWAYS);
            Button clear = new Button("清空列表");
            clear.setOnMouseClicked(e -> this.clearAll());
            clear.setStyle("-fx-padding: 0 5");

            HBox hBox = new HBox();
            hBox.getChildren().addAll(new Text("所有论文"), space, clear);

            rootNode = new TreeItem<>(hBox);
            rootNode.setExpanded(true);

            treeView.setRoot(rootNode);
            treeView.setPrefHeight(500);
        }
        checkBox.selectedProperty().addListener((observableValue, oldValue, newValue) ->
                this.rootNode.getChildren().forEach(node ->
                    ((FilenameEditorController)node).select(newValue)));
    }

    private List<FilenameEditorController> getSelectedNodes() {
        return  this.rootNode.getChildren().stream()
                .filter(node -> ((FilenameEditorController)node).selected())
                .map(node -> ((FilenameEditorController)node)).toList();

    }

    public void remove(FilenameEditorController child) {
        this.rootNode.getChildren().remove(child);
        this.files.remove(child.exportExistingFile());
    }

    public List<File> exportSelectedFiles() {
        return this.getSelectedNodes().stream().map(FilenameEditorController::exportExistingFile).toList();
    }

    public FileViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/FileView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void clearAll() {
        this.rootNode.getChildren().removeAll(this.rootNode.getChildren());
        this.files.clear();
    }
    private void setStatus(String text, String color) {
        this.status.setText(text);
        this.status.setTextFill(Color.valueOf(color));
    }

    @FXML
    protected void modify() {
        GetNameOptionsTask getNameOptionsTask = new GetNameOptionsTask(this.getSelectedNodes());
        getNameOptionsTask.setOnSucceeded(e -> this.setStatus("获取成功", "green"));
        getNameOptionsTask.setOnFailed(e -> this.setStatus("该文件错误", "red"));
        getNameOptionsTask.setOnRunning(e -> this.setStatus("获取论文标题中...", "black"));

        Thread thread = new Thread(getNameOptionsTask);
        thread.start();
        //TODO:thread pool
    }

    @FXML
    protected void rename() {
        this.getSelectedNodes().forEach(filenameEditor ->
                this.files.remove(filenameEditor.exportExistingFile()));
        RenameTask renameTask = new RenameTask(this.getSelectedNodes());
        renameTask.setOnSucceeded(e -> {
            this.setStatus("重命名成功", "green");
            this.getSelectedNodes().forEach(filenameEditor ->
                    this.files.add(filenameEditor.exportExistingFile()));
        });
        renameTask.setOnFailed(e -> {
            this.setStatus("该文件重命名失败", "red");
            this.getSelectedNodes().forEach(filenameEditor ->
                    this.files.add(filenameEditor.exportExistingFile()));
        });
        renameTask.setOnRunning(e -> this.setStatus("正在重命名", "black"));

        Thread thread = new Thread(renameTask);
        thread.start();
    }
}
