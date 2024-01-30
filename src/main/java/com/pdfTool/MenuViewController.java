package com.pdfTool;

import com.pdfTool.MenuFunctions.ExportFileViewController;
import com.pdfTool.MenuFunctions.MergeFileViewController;
import com.pdfTool.MenuFunctions.PrintViewController;
import com.pdfTool.defination.ExportType;
import com.pdfTool.utils.FileChooserUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MenuViewController extends HBox {
    public MenuViewController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/MenuView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    protected void importPaper(){
        List<File> files = FileChooserUtil.getFiles(this.getScene().getWindow());
        if(files==null) return;
    }

    @FXML
    protected void mergePDF(){
        MergeFileViewController merge = new MergeFileViewController();
        merge.show();
    }

    @FXML
    protected void splitPDF(){
        ExportFileViewController splitFile = new ExportFileViewController(ExportType.SPLIT);
        splitFile.show();
    }

    @FXML
    protected void print(){
        PrintViewController printFile = new PrintViewController();
        printFile.show();
    }

    @FXML
    protected void getPicture(){
        ExportFileViewController splitFile = new ExportFileViewController(ExportType.IMAGE);
        splitFile.show();
    }

    @FXML
    protected void getText(){
        ExportFileViewController splitFile = new ExportFileViewController(ExportType.TEXT);
        splitFile.show();
    }

    @FXML
    protected void getHelper(){

    }
}
