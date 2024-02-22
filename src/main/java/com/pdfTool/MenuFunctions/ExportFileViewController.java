package com.pdfTool.MenuFunctions;

import com.pdfTool.components.ExportFileItemController;
import com.pdfTool.components.RemovableItemController;
import com.pdfTool.defination.ExportItem;
import com.pdfTool.defination.ExportType;
import com.pdfTool.utils.ExportFileTask;
import com.pdfTool.utils.FileChooserUtil;
import com.pdfTool.utils.FileUtil;
import com.pdfTool.utils.PDFUtil;
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
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
            ExportFileItemController pageChooser = new ExportFileItemController(file, this.exportType);
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
    private boolean isexportAsOneFile() {
        return this.exportAsOneFile.isSelected();
    }
    private List<ExportFileItemController> getPageChoosers() {
        return this.pageChooser.getChildren().stream().map(node -> {
            Node pageChooser = ((RemovableItemController)node).getChild();
            return ((ExportFileItemController)pageChooser);
        }).toList();
    }
    private void init(ExportType type) {
        this.exportType = type;
        this.setOnMouseClicked(e -> this.requestFocus());

        exportAsOneFile.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            this.getPageChoosers().forEach(pageChooser -> {
                pageChooser.setExportAsMutiFiles(newValue);
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
            ExportFileItemController pageChooser = new ExportFileItemController(file, this.exportType);
            RemovableItemController item = new RemovableItemController(this.pageChooser, pageChooser);
            pageChooser.prefWidthProperty().bind(item.widthProperty().add(-50));
            this.pageChooser.getChildren().add(item);
        });
        stage.sizeToScene();
    }
    @FXML
    protected void export() {
        this.setStatus("", "black");
        List<ExportItem> exportItems = new ArrayList<>();
        boolean flag = false;
        for(ExportFileItemController exportFileItemController :this.getPageChoosers()) {
            ExportItem exportItem = exportFileItemController.getExportItem();
            if(exportItem.getSelectedPages() == null) {
                flag = true;
                continue;
            }
            exportItems.add(exportItem);
        }
        if(flag) {
            this.setStatus("导出页数非法", "red");
            return;
        }

        ExportFileTask exportFileTask = new ExportFileTask(exportItems, this.exportType,
                this.getDirectory(), this.isexportAsOneFile());
        exportFileTask.setOnSucceeded(e -> this.setStatus("导出成功", "green"));
        exportFileTask.setOnFailed(e -> this.setStatus("导出失败", "red"));
        exportFileTask.run();
        this.setStatus("导出中", "black");
    }

    @FXML
    protected void openDirectory() {
        File dir = FileChooserUtil.getDirectory(this.getScene().getWindow());
        if(dir==null) return;
        this.directory.setText(dir.getPath());
    }
}
