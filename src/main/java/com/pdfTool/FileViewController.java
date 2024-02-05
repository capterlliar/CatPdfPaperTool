package com.pdfTool;

import com.pdfTool.components.FilenameEditorController;
import com.pdfTool.defination.Paper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.io.File;
import java.util.HashMap;
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
    ScrollPane scrollPane;
    @FXML
    CheckBox checkBox;
    TreeItem<HBox> rootNode = null;
    HashSet<File> files;
    Integer cnt=0;

    public void loadPaper(List<Paper> papers) {
        for(Paper paper:papers){
            // Prevent duplicated paper.
            if(files.contains(paper.getFile()))
                continue;
            files.add(paper.getFile());

            paper.setId(cnt++);
            FilenameEditorController content = new FilenameEditorController(paper, this);
            rootNode.getChildren().add(content);
        }
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

            TreeView<HBox> treeView = new TreeView<>();
            treeView.setRoot(rootNode);
            treeView.setPrefHeight(500);

            scrollPane.setContent(treeView);
        }
        // Forbid horizontal scroll event of scrollPane.
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });
        checkBox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            this.rootNode.getChildren().forEach(node -> {
                ((FilenameEditorController)node).select(newValue);
            });
        });
    }

    public List<FilenameEditorController> getSelectedNodes() {
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

    protected void clearAll() {
        this.rootNode.getChildren().removeAll(this.rootNode.getChildren());
        this.files.clear();
    }


    @FXML
    protected void modify() {
        this.getSelectedNodes().forEach(FilenameEditorController::modify);
    }

    @FXML
    protected void rename() {
        this.getSelectedNodes().forEach(node -> {
            this.files.remove(node.exportExistingFile());
            node.rename();
            this.files.add(node.exportExistingFile());
        });
    }
}
