package com.pdfTool;

import eu.patrickgeiger.fxpdf.nodes.viewer.AppearanceType;
import eu.patrickgeiger.fxpdf.nodes.viewer.SampleViewer;
import eu.patrickgeiger.fxpdf.util.PDF;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;

public class PDFViewController extends HBox {
    public PDFViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/PDFView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            PDF pdf = new PDF(new File("D:\\IntellijIDEA\\fxPDF-master\\src\\main\\resources\\2311.09908.pdf"));
            SampleViewer sv = new SampleViewer(pdf);
            sv.setPrefWidth(800);
            sv.setTheme(AppearanceType.DARK);
            this.getChildren().add(sv);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
