package com.pdfTool;

import com.pdfTool.components.PDFViewer.PDFViewer;
import com.pdfTool.defination.PDF;
import com.pdfTool.utils.TimeUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.extern.log4j.Log4j;

import java.io.File;

@Log4j
public class PDFViewController extends HBox {
    private static PDFViewController INSTANCE = null;
    public static PDFViewController getInstance() {
        return INSTANCE;
    }
    PDFViewer pdfViewer = null;
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
            if (this.pdfViewer ==null){
                pdfViewer = new PDFViewer(pdf);
            }
            else {
                pdfViewer.loadPDF(pdf);
            }
            pdfViewer.setPrefWidth(10000);
            this.getChildren().add(pdfViewer);
            TimeUtil.end();
        } catch (Exception e) {
            Label label = new Label("打开失败，请检查文件格式");
            this.getChildren().add(label);
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
