package com.pdfTool;

import com.pdfTool.components.ExportFileViewController;
import com.pdfTool.defination.ExportType;
import com.pdfTool.utils.FileUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MenuViewController extends HBox {
    private String initialDir = System.getProperty("user.home");
    private FileChooser fileChooser = null;
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
        if(fileChooser==null) {
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF", "*.pdf")
            );
        }
        this.fileChooser.setInitialDirectory(new File(this.initialDir));
        List<File> files = fileChooser.showOpenMultipleDialog(this.getScene().getWindow());
        if (files!=null) {
            this.initialDir = FileUtil.getFileDirectory(files.get(0));
        }
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
        System.out.println("clicked.");
    }

    @FXML
    protected void getText(){
        System.out.println("clicked.");
    }
}
