package com.pdfTool;

import com.pdfTool.components.FilenameEditorController;
import com.pdfTool.defination.Paper;
import com.pdfTool.utils.FileUtil;
import com.pdfTool.utils.PDFUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
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
    HashMap<String, Integer> pathToId = null;
    Integer cnt=0;

    public void loadPaper(List<Paper> papers) {
        for(Paper paper:papers){
            // Prevent duplicated paper.
            if(pathToId.containsKey(paper.getPath()))
                continue;
            pathToId.put(paper.getPath(),paper.getId());

            paper.setId(cnt++);
            FilenameEditorController content = new FilenameEditorController(paper, rootNode);
            rootNode.getChildren().add(content);
        }
    }

    private void init() {
        INSTANCE = this;
        // To initialize treeView.
        if(rootNode==null) {
            pathToId = new HashMap<>();

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

    public FileViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/FileView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init();
            test();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    protected void clearAll() {
        this.rootNode.getChildren().removeAll(this.rootNode.getChildren());
    }

    @FXML
    protected void modify() {
        List<FilenameEditorController> selectedNodes = new ArrayList<>();
        this.rootNode.getChildren().forEach(node -> {
            if(((FilenameEditorController) node).selected()) {
                selectedNodes.add(((FilenameEditorController) node));
            }
        });
        if(selectedNodes.isEmpty()) return;
        selectedNodes.forEach(node -> {
            PDFUtil.setNewName(node.getPaper());
            node.apply();
        });
    }

    @FXML
    protected void rename() {
        this.rootNode.getChildren().forEach(node -> {
            if(((FilenameEditorController) node).selected()) {
                boolean renamed = FileUtil.rename(((FilenameEditorController) node).export());
                if(renamed) {
                    ((FilenameEditorController) node).select(false);
                }
                else {
                    //TODO:重命名失败警告弹窗
                }
            }
        });
    }

    private void test(){
        ArrayList<Paper>arrayList  = new ArrayList<>();
        arrayList.add(new Paper("test1","ARM-Net: Attention-guided residual multiscale CNN for multiclass brain tumor classification using MR images"));
        arrayList.add(new Paper("test2","Reinforcement learning for question answering in programming domain using public community scoring as a human feedback"));
        arrayList.add(new Paper("test3","Data-driven grapheme-to-phoneme representations for a lexicon-free text-to-speech"));
        loadPaper(arrayList);
    }
}
