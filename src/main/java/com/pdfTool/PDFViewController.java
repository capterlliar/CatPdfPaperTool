package com.pdfTool;

import eu.patrickgeiger.fxpdf.nodes.viewer.AppearanceType;
import eu.patrickgeiger.fxpdf.nodes.viewer.SampleViewer;
import eu.patrickgeiger.fxpdf.util.PDF;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.File;

public class PDFViewController extends HBox {
    private static PDFViewController INSTANCE = null;
    public static PDFViewController getInstance() {
        return INSTANCE;
    }
    SampleViewer sampleViewer = null;
    public PDFViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/PDFView.fxml"));
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
        INSTANCE = this;
        this.getChildren().add(new Label("预览PDF"));
    }
    public void loadPDF(String path){
        this.getChildren().removeAll(this.getChildren());
        try {
            PDF pdf = new PDF(new File(path));
            if (this.sampleViewer==null){
                sampleViewer = new SampleViewer(pdf);
            }
            else {
                sampleViewer.loadPDF(pdf);
            }
            sampleViewer.setPrefWidth(10000);
            sampleViewer.setTheme(AppearanceType.DARK);
            this.getChildren().add(sampleViewer);
        } catch (Exception e) {
            Label label = new Label("打开失败，请检查文件格式");
            this.getChildren().add(label);
        }
    }
}
