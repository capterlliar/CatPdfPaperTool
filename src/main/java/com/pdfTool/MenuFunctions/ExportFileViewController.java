package com.pdfTool.MenuFunctions;

import com.pdfTool.components.FilenameEditorController;
import com.pdfTool.components.PageChooserController;
import com.pdfTool.components.RemovableItemController;
import com.pdfTool.defination.ExportType;
import com.pdfTool.defination.Paper;
import com.pdfTool.utils.FileChooserUtil;
import com.pdfTool.utils.FileUtil;
import com.pdfTool.utils.PDFUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportFileViewController extends VBox {
    @FXML
    VBox pageChooser;
    @FXML
    TextField directory;
    @FXML
    CheckBox exportAsOneFile;
    ExportType exportType;
    Stage stage;
    public void show() {
        Scene scene = new Scene(this);

        if(stage==null) {
            stage = new Stage();
            stage.initOwner(this.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pdfTool/images/cat.png")));
        }
        stage.setTitle(this.exportType.getWindowName());
        stage.setScene(scene);
        stage.show();
    }
    public void addFile(List<File> files){
        files.forEach(file -> {
            PageChooserController pageChooser = new PageChooserController(file, this.exportType);
            RemovableItemController item = new RemovableItemController(this.pageChooser, pageChooser);
            pageChooser.prefWidthProperty().bind(item.widthProperty());
            this.pageChooser.getChildren().add(item);
        });
    }
    protected String getDirectory() {
        String directory = this.directory.getText();
        if(!FileUtil.checkAndCreateDir(directory)) {
            //TODO:文件夹不合法警告
        }
        if(!directory.endsWith(File.separator)) directory += File.separator;
        return directory;
    }

    private boolean exportAsOneFileSelected() {
        return this.exportAsOneFile.isSelected();
    }

    private void exportAsOneFile() {
        List<File> fileList = new ArrayList<>();
        try {
            for(Node node: this.pageChooser.getChildren()) {
                Node pageChooser = ((RemovableItemController)node).getChild();
                File file = ((PageChooserController)pageChooser).export(this.getClass().getResource("/com/pdfTool/testfiles").getPath());
                fileList.add(file);
            }
            String firstFilename = fileList.get(0).getName().split(" ")[1];
            String newFilename = firstFilename.substring(0, Math.min(10,firstFilename.length()))+"...等"+fileList.size()+"个文件.pdf";
            PDFUtil.mergeFiles(this.getDirectory()+newFilename, fileList);
            fileList.forEach(File::delete);
        }
        catch (Exception e) {
            //TODO
        }
    }

    private void exportAsMutiFiles() {
        try {
            for(Node node: this.pageChooser.getChildren()) {
                Node pageChooser = ((RemovableItemController)node).getChild();
                ((PageChooserController)pageChooser).export(this.getDirectory());
            }
        }
        catch (Exception e) {
            //TODO
        }
    }
    private void init(ExportType type) {
        this.exportType = type;
        this.setOnMouseClicked(e -> this.requestFocus());

        exportAsOneFile.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            this.pageChooser.getChildren().forEach(node -> {
                Node pageChooser = ((RemovableItemController)node).getChild();
                ((PageChooserController)pageChooser).setExportAsMutiFiles(newValue);
            });
        });
    }
    public ExportFileViewController(ExportType type) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ExportFileView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init(type);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    @FXML
    protected void addFile() {
        List<File> files = FileChooserUtil.getFiles(this.getScene().getWindow());
        if(files==null) return;
        files.forEach(file -> {
            PageChooserController pageChooser = new PageChooserController(file, this.exportType);
            RemovableItemController item = new RemovableItemController(this.pageChooser, pageChooser);
            pageChooser.prefWidthProperty().bind(item.widthProperty().add(-50));
            this.pageChooser.getChildren().add(item);
            stage.sizeToScene();
        });
    }
    @FXML
    protected void export() {
        if(this.exportAsOneFileSelected()) this.exportAsOneFile();
        else this.exportAsMutiFiles();
    }

    @FXML
    protected void openDirectory() {
        File dir = FileChooserUtil.getDirectory(this.getScene().getWindow());
        if(dir==null) return;
        this.directory.setText(dir.getPath());
    }
}
