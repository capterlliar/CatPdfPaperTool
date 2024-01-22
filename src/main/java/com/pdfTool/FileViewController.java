package com.pdfTool;

import com.pdfTool.components.SingleFileViewController;
import com.pdfTool.defination.Paper;
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
            SingleFileViewController content = new SingleFileViewController(paper.getFilename());
            content.getTextArea().prefWidthProperty().bind(this.widthProperty().add(-170));
            TreeItem<HBox> node = new TreeItem<>(content);
            rootNode.getChildren().add(node);
        }
    }

    private void init() {
        scrollPane.setFitToWidth(true);
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
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
        arrayList.add(new Paper("test1","ARM-Net: Attention-guided residual multiscale CNN for multiclass brain tumor classification using MR images"));
        arrayList.add(new Paper("test2","Reinforcement learning for question answering in programming domain using public community scoring as a human feedback"));
        arrayList.add(new Paper("test3","Data-driven grapheme-to-phoneme representations for a lexicon-free text-to-speech"));
        loadPaper(arrayList);
    }
}
