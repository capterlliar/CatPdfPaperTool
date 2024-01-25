package com.pdfTool;

import com.pdfTool.components.ExportFileViewController;
import com.pdfTool.defination.ExportType;
import com.pdfTool.utils.FileChooserUtil;
import com.pdfTool.utils.FileUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

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
        System.out.println("clicked.");
    }

    @FXML
    protected void splitPDF(){
        ExportFileViewController splitFile = new ExportFileViewController(ExportType.SPLIT);
        splitFile.show();
    }

    @FXML
    protected void print(){
        System.out.println("clicked.");
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
