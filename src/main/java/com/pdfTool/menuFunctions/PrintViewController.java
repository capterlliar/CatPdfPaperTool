package com.pdfTool.menuFunctions;

import com.pdfTool.components.FileItem;
import com.pdfTool.components.FileList;
import com.pdfTool.components.RemovableItem;
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
import lombok.extern.log4j.Log4j;

import javax.print.PrintService;
import java.io.File;
import java.util.List;
import java.util.Objects;

@Log4j
public class PrintViewController extends VBox {
    @FXML
    FileList fileList;
    @FXML
    ChoiceBox<PrintService> printerChoiceBox;
    @FXML
    TextField copy;
    @FXML
    Label status;
    Stage stage;
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
    private void init() {
        List<PrintService> printServices = PrinterUtil.getPrintServices();
        this.printerChoiceBox.getItems().addAll(printServices);
        this.printerChoiceBox.setValue(printServices.get(0));
        this.setOnMouseClicked(e -> this.requestFocus());
    }
    public void show() {
        Scene scene = new Scene(this);

        if(stage==null) {
            stage = new Stage();
            stage.initOwner(this.getScene().getWindow());
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/pdfTool/images/cat.png"))));
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
    private List<FileItem> getFileItems() {
        return this.fileList.getFileContainer().getChildren().stream()
                .map(node -> {
                    Node node1 = ((RemovableItem)node).getChild();
                    return ((FileItem)node1);
                }).toList();
    }
    private List<File> getFiles() {
        return this.getFileItems().stream().map(FileItem::getFile).toList();
    }

    @FXML
    protected void print() {
        PrintService printService = this.getSelectedPrintService();
        Integer copies = this.getCopies();
        List<File> files = this.getFiles();
        if(printService == null || copies == null || files == null) return;
        if(files.isEmpty()) return;

        this.getFileItems().forEach(fileItem -> fileItem.setFilenameColor("black"));

        PrintFileTask printFileTask = new PrintFileTask(files, printService, copies);
        printFileTask.setOnSucceeded(e -> this.setStatus("打印成功", "green"));
        printFileTask.setOnFailed(e -> {
            this.setStatus("打印失败", "red");
            Throwable exc = printFileTask.getException();
            log.error(exc);
            exc.printStackTrace();
        });
        printFileTask.setOnRunning(e -> this.setStatus("打印中", "black"));
        printFileTask.messageProperty().addListener((observableValue, s, t1) ->
                this.getFileItems().forEach(fileItem -> {
                    if(fileItem.getFile().getName().equals(t1)) {
                        fileItem.setFilenameColor("green");
                    }
        }));
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
