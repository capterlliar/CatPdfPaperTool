package com.pdfTool;

import com.pdfTool.components.SingleFileItemController;
import com.pdfTool.defination.Paper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
        for(Paper paper:papers){
            if(pathToId.containsKey(paper.getPath()))
                continue;
            paper.setId(cnt++);
            pathToId.put(paper.getPath(),paper.getId());
            SingleFileItemController content = new SingleFileItemController(paper.getFilename());
            content.getTextArea().prefWidthProperty().bind(this.widthProperty().add(-170));
            TreeItem<HBox> node = new TreeItem<>(content);
            rootNode.getChildren().add(node);
        }
    }

    private void init() {
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

    protected void clearAll() {

    }

    private void test(){
        ArrayList<Paper>arrayList  = new ArrayList<>();
        arrayList.add(new Paper("test1","ARM-Net: Attention-guided residual multiscale CNN for multiclass brain tumor classification using MR images"));
        arrayList.add(new Paper("test2","Reinforcement learning for question answering in programming domain using public community scoring as a human feedback"));
        arrayList.add(new Paper("test3","Data-driven grapheme-to-phoneme representations for a lexicon-free text-to-speech"));
        loadPaper(arrayList);
    }
}
