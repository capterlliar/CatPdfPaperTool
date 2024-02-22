package com.pdfTool.components;

import com.pdfTool.defination.ExportItem;
import com.pdfTool.defination.ExportType;
import com.pdfTool.utils.FileUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;
import lombok.Getter;

import java.io.File;
import java.util.List;

public class ExportFileItemController extends GridPane {
    @FXML
    TextField textField;
    @FXML
    Text text;
    @FXML
    CheckBox exportAsMutiFiles;
    Label filename;
    ExportType exportType;
    @Getter
    File file;
    public void setExportAsMutiFiles(boolean select) {
        this.exportAsMutiFiles.setSelected(false);
        this.exportAsMutiFiles.setDisable(select);
    }
    public void setFilename(String text, String color) {
        this.filename.setText(text);
        this.filename.setTextFill(Color.valueOf(color));
    }
    public ExportItem getExportItem() {
        return new ExportItem(this.file, this.getSelectedPages(), this.isExportedAsMutiFiles());
    }
    private List<Pair<Integer, Integer>> getSelectedPages() {
        this.text.setFill(Color.valueOf("black"));
        List<Pair<Integer, Integer>> pages = FileUtil.getSelectedPages(this.textField.getText());
        if(pages == null) {
            this.text.setFill(Color.valueOf("red"));
            return null;
        }
        return pages;
    }
    private boolean isExportedAsMutiFiles() {
        return this.exportAsMutiFiles.isSelected();
    }
    private void init(File file, ExportType exportType) {
        this.exportType = exportType;
        this.file = file;

        this.filename = new Label(file.getName());
        this.filename.setStyle("-fx-text-overrun: ellipsis;");
        this.filename.prefWidthProperty().bind(this.widthProperty().add(-100));
        GridPane.isFillWidth(this.filename);
        this.add(this.filename, 0,0);

        if(this.exportType == ExportType.IMAGE) {
            this.exportAsMutiFiles.setVisible(false);
        }
    }
    public ExportFileItemController(File file, ExportType exportType) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ExportFileItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init(file, exportType);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
