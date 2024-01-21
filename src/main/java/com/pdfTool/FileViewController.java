package com.pdfTool;

import com.pdfTool.components.SingleFileViewController;
import com.pdfTool.defination.Paper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class FileViewController extends BorderPane {
    @FXML
    ScrollPane scrollPane;
    TreeItem<HBox> rootNode = null;
    HashMap<String, Integer> pathToId = null;
    Integer cnt=0;

    public void loadPaper(ArrayList<Paper> papers) {
        if(rootNode==null) {
            pathToId = new HashMap<>();

            HBox hBox = new HBox();
            hBox.getChildren().add(new Text("所有论文"));
            rootNode = new TreeItem<>(hBox);
            rootNode.setExpanded(true);

            TreeView<HBox> treeView = new TreeView<>();
            treeView.setRoot(rootNode);
            treeView.setPrefHeight(500);

            scrollPane.setContent(treeView);
        }
        for(Paper paper:papers){
            if(pathToId.containsKey(paper.getPath()))
                continue;
            paper.setId(cnt++);
            pathToId.put(paper.getPath(),paper.getId());
            CheckBoxTreeItem<HBox> node = new CheckBoxTreeItem<>(
                    new SingleFileViewController(paper.getFilename()));
            rootNode.getChildren().add(node);
        }
    }

    private void init() {
        scrollPane.setFitToWidth(true);
        scrollPane.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
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

    private void test(){
        ArrayList<Paper>arrayList  = new ArrayList<>();
        arrayList.add(new Paper("test1",
                "ARM-Net: Attention-guided residual multiscale CNN for multiclass brain tumor classification using MR images"));
        arrayList.add(new Paper("test2","test2"));
        loadPaper(arrayList);
    }
}
