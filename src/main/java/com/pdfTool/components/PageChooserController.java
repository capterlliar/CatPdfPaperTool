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
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageChooserController extends GridPane {
    @FXML
    TextField textField;
    @FXML
    CheckBox exportAsMutiFiles;
    ExportType exportType;
    File file;
    public void setExportAsMutiFiles(boolean select) {
        this.exportAsMutiFiles.setSelected(false);
        this.exportAsMutiFiles.setDisable(select);
    }
    public String getText() {
        return this.textField.getText();
    }
    public boolean exportAsMutiFiles() {
        return this.exportAsMutiFiles.isSelected();
    }

    public List<Pair<Integer, Integer>> getSelectedPages(String s) {
        List<Pair<Integer, Integer>> res = new ArrayList<>();
        //输入为空，默认导出整个文件
        if(s.trim().equals("")) return res;

        String[] s1 = s.split(",");
        for(String s2: s1) {
            s2 = s2.trim();
            String[] s3 = s2.split("-");
            //e.g. "9"
            if(s3.length == 1){
                int i = Integer.parseInt(s3[0].trim());
                res.add(new Pair<>(i, i));
            }
            //e.g. "3-4", "-4"
            else if(s3.length == 2){
                int from = Integer.parseInt(s3[0].trim());
                int to = Integer.parseInt(s3[1].trim());
                if(from <= to) res.add(new Pair<>(from,to));
                else {
                    //TODO: wrong type;
                    //TODO: check page range;
                }
            }
            else {
                //TODO: wrong type
            }
        }
        return res;
    }
    public File export(String dest) throws IOException {
        List<Pair<Integer, Integer>> pages = this.getSelectedPages(this.getText());

        File res = null;
        switch (this.exportType) {
            case SPLIT -> {
                if(pages.isEmpty()) return this.file;

                List<File> files = PDFUtil.splitAsMutiFiles(pages, this.file, dest);
                if(!this.exportAsMutiFiles()) {
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
                if(!this.exportAsMutiFiles()) dest = dest + FileUtil.getPDFFilename(this.file) + "/";
                PDFUtil.getImages(pages, this.file, dest);
            }
        }
        return res;
    }
    private void init(File file, ExportType exportType) {
        this.exportType = exportType;
        this.file = file;
        Label label = new Label(file.getName());
        label.setStyle("-fx-text-overrun: ellipsis;");
        label.prefWidthProperty().bind(this.widthProperty().add(-100));
        GridPane.isFillWidth(label);
        this.add(label, 0,0);
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

    //TODO:字符校验
}
