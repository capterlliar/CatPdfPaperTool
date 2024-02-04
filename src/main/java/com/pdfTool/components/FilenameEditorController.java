package com.pdfTool.components;

import com.pdfTool.defination.Paper;
import com.pdfTool.utils.FileUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import lombok.Getter;

import java.io.File;
import java.util.List;

public class FilenameEditorController extends TreeItem<HBox> {
    @FXML
    TextArea textArea;
    @FXML
    HBox value;
    @FXML
    CheckBox checkBox;
    @Getter
    Paper paper;
    TreeItem<HBox> parent;
    public String getText() {
        return this.textArea.getText();
    }
    public void setText(String s) {
        this.textArea.setText(s);
    }
    public boolean selected() {
        return this.checkBox.isSelected();
    }
    public void select(boolean select) {
        this.checkBox.setSelected(select);
    }
    public void chooseOption(String text) {
        this.setText(text);
        this.setExpanded(false);
    }
    public void apply() {
        this.setText(this.paper.getNewname());
        this.getChildren().removeAll(this.getChildren());
        List<String> options = this.paper.getOptions();
        if (options != null) {
            options.forEach(option ->
                    this.getChildren().add(new FilenameOptionController(option, this)));
        }
        this.setExpanded(false);
    }
    public Pair<File, File> export() {
        return new Pair<>(new File(paper.getPath()),
                new File(FileUtil.getDirectory(paper.getPath())+File.separator+this.getText()));
    }

    private void init(Paper paper, TreeItem<HBox> parent) {
        this.paper = paper;
        this.parent = parent;
        this.setText(this.paper.getFilename());
        this.textArea.prefWidthProperty().bind(this.value.widthProperty().add(-100));
    }
    public FilenameEditorController(Paper paper, TreeItem<HBox> parent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FilenameEditor.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init(paper,parent);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    protected void openPDF() {

    }

    @FXML
    protected void remove() {
        this.parent.getChildren().remove(this);
    }
}
