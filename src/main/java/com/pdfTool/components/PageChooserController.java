package com.pdfTool.components;

import com.pdfTool.defination.ExportType;
import com.pdfTool.utils.FileUtil;
import com.pdfTool.utils.PDFUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PageChooserController extends GridPane {
    @FXML
    TextField textField;
    @FXML
    Text text;
    @FXML
    CheckBox exportAsMutiFiles;
    Label filename;
    ExportType exportType;
    File file;
    public void setExportAsMutiFiles(boolean select) {
        this.exportAsMutiFiles.setSelected(false);
        this.exportAsMutiFiles.setDisable(select);
    }
    public boolean formatCheck() {
        this.text.setFill(Color.valueOf("black"));
        List<Pair<Integer, Integer>> pages = FileUtil.getSelectedPages(this.getText());
        if(pages == null) {
            this.text.setFill(Color.valueOf("red"));
            return false;
        }
        return true;
    }
    public File export(String dest) throws IOException {
        this.filename.setTextFill(Color.valueOf("black"));

        File res = null;
        List<Pair<Integer, Integer>> pages = FileUtil.getSelectedPages(this.getText());
        assert pages != null;
        try {
            switch (this.exportType) {
                case SPLIT -> {
                    if(pages.isEmpty()) return this.file;

                    List<File> files = PDFUtil.splitAsMutiFiles(pages, this.file, dest);
                    if(this.isExportedAsMutiFiles()) {
                        String newFilename = FileUtil.getUniqueFilename(dest, this.file.getName());
                        PDFUtil.mergeFiles(newFilename, files);
                        res = new File(newFilename);
                        files.forEach(File::delete);
                    }
                }
                case TEXT -> {
                    PDFUtil.getText(pages, this.file, dest);
                }
                case IMAGE -> {
                    //pages.isEmpty will be handled in the following function.
                    if(this.isExportedAsMutiFiles()) dest = dest + FileUtil.getPDFFilename(this.file) + "/";
                    PDFUtil.getImages(pages, this.file, dest);
                }
            }
            this.filename.setTextFill(Color.valueOf("green"));
        } catch (Exception e) {
            this.filename.setTextFill(Color.valueOf("red"));
            throw new RuntimeException();
        }

        return res;
    }
    private String getText() {
        return this.textField.getText();
    }
    private boolean isExportedAsMutiFiles() {
        return !this.exportAsMutiFiles.isSelected();
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
    public PageChooserController(File file, ExportType exportType) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PageChooser.fxml"));
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
