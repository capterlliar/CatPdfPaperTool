package com.pdfTool;

import com.pdfTool.components.SingleFileViewController;
import com.pdfTool.defination.Paper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class FileViewController extends BorderPane {
    @FXML
    ScrollPane scrollPane;
    TreeItem<BorderPane> rootNode = null;
    HashMap<String, Integer> pathToId = null;
    Integer cnt=0;

    public void loadPaper(ArrayList<Paper> papers) {
        if(rootNode==null) {
            pathToId = new HashMap<>();

            BorderPane borderPane = new BorderPane();
            borderPane.setLeft(new Text("所有论文"));
            rootNode = new TreeItem<>(borderPane);
            rootNode.setExpanded(true);

            TreeView<BorderPane> treeView = new TreeView<>();
            treeView.setRoot(rootNode);
            treeView.setPrefHeight(500);
            //TODO: remove tree node border when clicked.

            scrollPane.setContent(treeView);
        }
        for(Paper paper:papers){
            if(pathToId.containsKey(paper.getPath()))
                continue;
            paper.setId(cnt++);
            pathToId.put(paper.getPath(),paper.getId());
            CheckBoxTreeItem<BorderPane> node = new CheckBoxTreeItem<>(
                    new SingleFileViewController(paper.getFilename()));
            rootNode.getChildren().add(node);
        }
    }

    public FileViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/FileView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            scrollPane.setFitToWidth(true);
            test();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void test(){
        ArrayList<Paper>arrayList  = new ArrayList<>();
        arrayList.add(new Paper("test1","test1"));
        arrayList.add(new Paper("test2","test2"));
        loadPaper(arrayList);
    }
}
