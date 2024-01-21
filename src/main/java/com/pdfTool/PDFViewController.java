package com.pdfTool;

import eu.patrickgeiger.fxpdf.nodes.viewer.AppearanceType;
import eu.patrickgeiger.fxpdf.nodes.viewer.SampleViewer;
import eu.patrickgeiger.fxpdf.util.PDF;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;

public class PDFViewController extends HBox {
    SampleViewer sampleViewer = null;

    public void loadPDF(String path) throws IOException {
        PDF pdf = new PDF(new File(path));
        if (this.sampleViewer==null){
            sampleViewer = new SampleViewer(pdf);
        }
        else {
            //TODO: add a method to fxPDF to open a new file.
            //TODO: also adjust default zoom size to 30.
        }
        sampleViewer.setPrefWidth(10000);
        sampleViewer.setTheme(AppearanceType.DARK);
        this.getChildren().add(sampleViewer);
    }

    public PDFViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/PDFView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            test();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void test() throws IOException {
        loadPDF("D:\\IntellijIDEA\\fxPDF-master\\src\\main\\resources\\2311.09908.pdf");
    }
}
