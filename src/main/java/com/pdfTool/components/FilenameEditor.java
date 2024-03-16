package com.pdfTool.components;

import com.pdfTool.FileViewController;
import com.pdfTool.PDFViewController;
import com.pdfTool.defination.RenameItem;
import com.pdfTool.utils.FileUtil;
import com.pdfTool.utils.PDFUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.util.List;

@Log4j
public class FilenameEditor extends TreeItem<HBox> {
    @FXML
    TextArea textArea;
    @FXML
    HBox value;
    @FXML
    CheckBox checkBox;
    @FXML
    Label warning;
    RenameItem renameItem;
    FileViewController parent;
    public FilenameEditor(RenameItem renameItem, FileViewController parent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FilenameEditor.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init(renameItem,parent);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    private void init(RenameItem renameItem, FileViewController parent) {
        this.renameItem = renameItem;
        this.parent = parent;
        this.warning.setVisible(false);
        this.setText(renameItem.getFile().getName());
        this.textArea.prefWidthProperty().bind(this.value.widthProperty().add(-120));
        this.expandedProperty().addListener((observableValue, aBoolean, t1) -> this.parent.focusOn(this));
    }
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
    }
    public String getNewPath() {
        String newPath = FileUtil.getFileDirectory(renameItem.getFile())+File.separator+this.getText();
        if(!newPath.endsWith(".pdf")) newPath += ".pdf";
        return newPath;
    }
    public File exportNewFile() {
        return new File(this.getNewPath());
    }
    public File exportExistingFile() {
        return this.renameItem.getFile();
    }
    public void rename() throws RuntimeException {
        this.warning.setVisible(false);

        File newFile = this.exportNewFile();
        boolean renamed = FileUtil.rename(this.exportExistingFile(), newFile);
        if(renamed) {
            this.select(false);
            this.renameItem.setFile(newFile);
            this.getChildren().removeAll(this.getChildren());
        }
        else {
            this.warning.setVisible(true);
            throw new RuntimeException();
        }
    }
    public void modify() throws RuntimeException {
        this.warning.setVisible(false);
        this.getChildren().removeAll(this.getChildren());
        try {
            PDFUtil.setNewName(this.renameItem);
        } catch (Exception e) {
            this.warning.setVisible(true);
            throw new RuntimeException();
        }
        List<String> options = this.renameItem.getOptions();
        if (options != null) {
            if(options.size() > 0) {
                this.setText(options.get(0));
                for(int i=1;i<options.size();i++){
                    this.getChildren().add(new FilenameOption(options.get(i), this));
                }
            }
        }
        this.setExpanded(false);
    }

    @FXML
    protected void openPDF() {
        PDFViewController.getInstance().loadPDF(this.renameItem.getFile().getPath());
        this.parent.focusOn(this);
    }
    @FXML
    protected void remove() {
        this.parent.remove(this);
    }
}
