package com.pdfTool.MenuFunctions;

import com.pdfTool.components.PageChooserController;
import com.pdfTool.components.RemovableItemController;
import com.pdfTool.defination.ExportType;
import com.pdfTool.utils.FileChooserUtil;
import com.pdfTool.utils.FileUtil;
import com.pdfTool.utils.PDFUtil;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExportFileViewController extends VBox {
    @FXML
    VBox pageChooser;
    @FXML
    TextField directory;
    @FXML
    CheckBox exportAsOneFile;
    @FXML
    Label text1;
    @FXML
    Label text2;
    @FXML
    Label status;
    ExportType exportType;
    Stage stage;
    public void show() {
        Scene scene = new Scene(this);

        if(stage==null) {
            stage = new Stage();
            stage.initOwner(this.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(
                        Objects.requireNonNull(
                            getClass().getResourceAsStream("/com/pdfTool/images/cat.png"))));
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
    private void setStatus(String text, String color) {
        this.status.setText(text);
        this.status.setTextFill(Color.valueOf(color));
    }
    private String getDirectory() {
        String directory = this.directory.getText();
        if(!FileUtil.checkAndCreateDir(directory)) {
            this.setStatus("导出目录名非法", "red");
        }
        if(!directory.endsWith(File.separator)) directory += File.separator;
        return directory;
    }

    private boolean exportAsOneFileSelected() {
        return this.exportAsOneFile.isSelected();
    }

    private void exportAsOneFile() throws IOException {
        List<File> fileList = new ArrayList<>();
        for(Node node: this.pageChooser.getChildren()) {
            Node pageChooser = ((RemovableItemController)node).getChild();
            File file = ((PageChooserController)pageChooser).export(Objects.requireNonNull(this.getClass().getResource("/com/pdfTool/testfiles")).getPath());
            fileList.add(file);
        }
        String newFilename = FileUtil.getFileListName(fileList);
        PDFUtil.mergeFiles(this.getDirectory() + newFilename, fileList);
        fileList.forEach(File::delete);
    }

    private void exportAsMutiFiles() throws IOException {
        for(Node node: this.pageChooser.getChildren()) {
            Node pageChooser = ((RemovableItemController)node).getChild();
            ((PageChooserController)pageChooser).export(this.getDirectory());
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

        switch (this.exportType) {
            case SPLIT, TEXT -> this.text1.setText("将所有所选内容导出到一个文件  ");
            case IMAGE -> {
                this.text1.setText("将所有所选内容导出到一个目录  ");
                this.text2.setVisible(false);
            }
        }
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
        boolean formatError = false;
        for(Node node: this.pageChooser.getChildren()) {
            Node pageChooser = ((RemovableItemController)node).getChild();
            if(!((PageChooserController)pageChooser).formatCheck()) formatError = true;
        }
        if(formatError) {
            this.setStatus("导出页数非法", "red");
            return;
        }

        try {
            if(this.exportAsOneFileSelected()) this.exportAsOneFile();
            else this.exportAsMutiFiles();
        }
        catch (Exception e) {
            this.setStatus("导出失败", "red");
        }
    }

    @FXML
    protected void openDirectory() {
        File dir = FileChooserUtil.getDirectory(this.getScene().getWindow());
        if(dir==null) return;
        this.directory.setText(dir.getPath());
    }
}
