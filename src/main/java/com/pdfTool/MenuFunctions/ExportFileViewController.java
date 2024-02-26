package com.pdfTool.MenuFunctions;

import com.pdfTool.components.ExportFileItemController;
import com.pdfTool.components.RemovableItemController;
import com.pdfTool.defination.ExportItem;
import com.pdfTool.defination.ExportType;
import com.pdfTool.services.ExportFileTask;
import com.pdfTool.utils.FileChooserUtil;
import com.pdfTool.utils.FileUtil;
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
import java.util.*;

public class ExportFileViewController extends VBox {
    @FXML
    VBox exportFileItems;
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
            ExportFileItemController exportFileItem = new ExportFileItemController(file, this.exportType);
            RemovableItemController item = new RemovableItemController(this.exportFileItems, exportFileItem);
            exportFileItem.prefWidthProperty().bind(item.widthProperty());
            this.exportFileItems.getChildren().add(item);
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
            return null;
        }
        if(!directory.endsWith(File.separator)) directory += File.separator;
        return directory;
    }
    private boolean isexportAsOneFile() {
        return this.exportAsOneFile.isSelected();
    }
    private List<ExportFileItemController> getExportFileItems() {
        return this.exportFileItems.getChildren().stream().map(node -> {
            Node exportFileItem = ((RemovableItemController)node).getChild();
            return ((ExportFileItemController)exportFileItem);
        }).toList();
    }
    private void init(ExportType type) {
        this.exportType = type;
        this.setOnMouseClicked(e -> this.requestFocus());

        exportAsOneFile.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            this.getExportFileItems().forEach(exportFileItem -> {
                exportFileItem.setExportAsMutiFiles(newValue);
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
            ExportFileItemController exportFileItem = new ExportFileItemController(file, this.exportType);
            RemovableItemController item = new RemovableItemController(this.exportFileItems, exportFileItem);
            exportFileItem.prefWidthProperty().bind(item.widthProperty().add(-50));
            this.exportFileItems.getChildren().add(item);
        });
        stage.sizeToScene();
    }
    @FXML
    protected void export() {
        this.setStatus("", "black");
        List<ExportItem> exportItems = new ArrayList<>();
        boolean flag = false;
        for(ExportFileItemController exportFileItemController :this.getExportFileItems()) {
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
        String dir = this.getDirectory();
        if(dir == null) return;
        if(exportItems.isEmpty()) return;

        this.getExportFileItems().forEach(exportFileItem -> {
            exportFileItem.setFilenameColor("black");
        });

        ExportFileTask exportFileTask = new ExportFileTask(exportItems, this.exportType,
                dir, this.isexportAsOneFile());
        exportFileTask.setOnSucceeded(e -> this.setStatus("导出成功", "green"));
        exportFileTask.setOnFailed(e -> this.setStatus("导出失败", "red"));
        exportFileTask.messageProperty().addListener((observableValue, s, t1) -> {
            this.getExportFileItems().forEach(exportFileItem -> {
                if(exportFileItem.getFile().getName().equals(t1)) {
                    exportFileItem.setFilenameColor("green");
                }
            });
        });
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
