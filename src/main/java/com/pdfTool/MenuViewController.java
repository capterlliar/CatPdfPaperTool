package com.pdfTool;

import com.pdfTool.menuFunctions.ExportFileViewController;
import com.pdfTool.menuFunctions.MergeFileViewController;
import com.pdfTool.menuFunctions.PrintViewController;
import com.pdfTool.defination.ExportType;
import com.pdfTool.utils.FileChooserUtil;
import com.pdfTool.utils.FileUtil;
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
        FileViewController.getInstance().loadPaper(FileUtil.filesToPapers(files));
    }

    @FXML
    protected void importDir(){
        File dir = FileChooserUtil.getDirectory(this.getScene().getWindow());
        if(dir==null) return;
        List<File> files = FileUtil.GetDirFiles(dir);
        if(files == null) return;
        FileViewController.getInstance().loadPaper(FileUtil.filesToPapers(files));
    }

    @FXML
    protected void mergePDF(){
        MergeFileViewController merge = new MergeFileViewController();
        merge.addFile(FileViewController.getInstance().exportSelectedFiles());
        merge.show();
    }

    @FXML
    protected void splitPDF(){
        ExportFileViewController splitFile = new ExportFileViewController(ExportType.SPLIT);
        splitFile.addFile(FileViewController.getInstance().exportSelectedFiles());
        splitFile.show();
    }

    @FXML
    protected void print(){
        PrintViewController printFile = new PrintViewController();
        printFile.addFile(FileViewController.getInstance().exportSelectedFiles());
        printFile.show();
    }

    @FXML
    protected void getPicture(){
        ExportFileViewController splitFile = new ExportFileViewController(ExportType.IMAGE);
        splitFile.addFile(FileViewController.getInstance().exportSelectedFiles());
        splitFile.show();
    }

    @FXML
    protected void getText(){
        ExportFileViewController splitFile = new ExportFileViewController(ExportType.TEXT);
        splitFile.addFile(FileViewController.getInstance().exportSelectedFiles());
        splitFile.show();
    }

    @FXML
    protected void getHelper(){

    }
}
