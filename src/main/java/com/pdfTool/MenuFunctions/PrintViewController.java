package com.pdfTool.MenuFunctions;

import com.pdfTool.components.FileItemController;
import com.pdfTool.components.FileListController;
import com.pdfTool.components.RemovableItemController;
import com.pdfTool.services.PrintFileTask;
import com.pdfTool.utils.FileChooserUtil;
import com.pdfTool.utils.PrinterUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.print.PrintService;
import java.io.File;
import java.util.List;

public class PrintViewController extends VBox {
    @FXML
    FileListController fileList;
    @FXML
    ChoiceBox<PrintService> printerChoiceBox;
    @FXML
    TextField copy;
    @FXML
    Label status;
    Stage stage;
    public void show() {
        Scene scene = new Scene(this);

        if(stage==null) {
            stage = new Stage();
            stage.initOwner(this.getScene().getWindow());
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pdfTool/images/cat.png")));
        }
        stage.setTitle("打印所选项");
        stage.setScene(scene);
        stage.show();
    }
    private PrintService getSelectedPrintService() {
        return this.printerChoiceBox.getValue();
    }
    private Integer getCopies() {
        String s = this.copy.getText();
        if(!s.matches("[0-9]+")) return null;
        return Integer.parseInt(s);
    }
    private void setStatus(String text, String color) {
        this.status.setText(text);
        this.status.setTextFill(Color.valueOf(color));
    }
    private List<FileItemController> getFileItems() {
        return this.fileList.getFileContainer().getChildren().stream()
                .map(node -> {
                    Node node1 = ((RemovableItemController)node).getChild();
                    return ((FileItemController)node1);
                }).toList();
    }
    private List<File> getFiles() {
        return this.getFileItems().stream().map(FileItemController::getFile).toList();
    }
    private void init() {
        List<PrintService> printServices = PrinterUtil.getPrintServices();
        this.printerChoiceBox.getItems().addAll(printServices);
        this.printerChoiceBox.setValue(printServices.get(0));
        this.setOnMouseClicked(e -> this.requestFocus());
    }
    public PrintViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PrintView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    protected void print() {
        PrintService printService = this.getSelectedPrintService();
        Integer copies = this.getCopies();
        List<File> files = this.getFiles();
        if(printService == null || copies == null || files == null) return;
        if(files.isEmpty()) return;

        this.getFileItems().forEach(fileItemController -> {
            fileItemController.setFilenameColor("black");
        });

        PrintFileTask printFileTask = new PrintFileTask(files, printService, copies.intValue());
        printFileTask.setOnSucceeded(e -> this.setStatus("打印成功", "green"));
        printFileTask.setOnFailed(e -> this.setStatus("打印失败", "red"));
        printFileTask.setOnRunning(e -> this.setStatus("打印中", "black"));
        printFileTask.messageProperty().addListener((observableValue, s, t1) -> {
            this.getFileItems().forEach(fileItemController -> {
                if(fileItemController.getFile().getName().equals(t1)) {
                    fileItemController.setFilenameColor("green");
                }
            });
        });
        Thread thread = new Thread(printFileTask);
        thread.start();
    }

    @FXML
    protected void addFile() {
        List<File> files = FileChooserUtil.getFiles(this.getScene().getWindow());
        if(files==null) return;
        fileList.addFile(files);
        stage.sizeToScene();
    }
}
