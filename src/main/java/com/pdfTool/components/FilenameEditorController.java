package com.pdfTool.components;

import com.pdfTool.FileViewController;
import com.pdfTool.defination.Paper;
import com.pdfTool.utils.FileUtil;
import com.pdfTool.utils.PDFUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.List;

public class FilenameEditorController extends TreeItem<HBox> {
    @FXML
    TextArea textArea;
    @FXML
    HBox value;
    @FXML
    CheckBox checkBox;
    Paper paper;
    FileViewController parent;
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
    public String getNewPath() {
        String newPath = FileUtil.getFileDirectory(paper.getFile())+File.separator+this.getText();
        if(!newPath.endsWith(".pdf")) newPath += ".pdf";
        return newPath;
    }
    public File exportNewFile() {
        return new File(this.getNewPath());
    }

    public File exportExistingFile() {
        return this.paper.getFile();
    }

    public void rename() {
        File newFile = this.exportNewFile();
        boolean renamed = FileUtil.rename(this.exportExistingFile(), newFile);
        if(renamed) {
            this.select(false);
            this.paper.setFile(newFile);
        }
        else {
            //TODO:重命名失败警告
        }
    }

    public void modify() {
        this.getChildren().removeAll(this.getChildren());
        PDFUtil.setNewName(this.paper);

        List<String> options = this.paper.getOptions();
        if (options != null) {
            if(options.size() > 0) {
                this.setText(options.get(0));
                for(int i=1;i<options.size();i++){
                    this.getChildren().add(new FilenameOptionController(options.get(i), this));
                }
            }
        }
        this.setExpanded(false);
    }
    private void init(Paper paper, FileViewController parent) {
        this.paper = paper;
        this.parent = parent;
        this.setText(paper.getFile().getName());
        this.textArea.prefWidthProperty().bind(this.value.widthProperty().add(-100));
    }
    public FilenameEditorController(Paper paper, FileViewController parent) {
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
        this.parent.remove(this);
    }
}
