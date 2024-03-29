package com.pdfTool.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;

public class FilenameOption extends TreeItem<HBox> {
    @FXML
    TextArea textArea;
    @FXML
    HBox value;
    FilenameEditor parent;
    public FilenameOption(String fileNameOption, FilenameEditor parent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FilenameOption.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init(fileNameOption, parent);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    private void init(String fileNameOption, FilenameEditor parent) {
        this.parent = parent;
        this.setText(fileNameOption);
        this.textArea.prefWidthProperty().bind(this.value.widthProperty().add(-85));
    }
    public String getText() {
        return this.textArea.getText();
    }
    public void setText(String s) {
        this.textArea.setText(s);
    }

    @FXML
    protected void exchange() {
        String newText = this.parent.getText();
        String oldText = this.getText();
        this.setText(newText);
        this.parent.chooseOption(oldText);
    }
}
