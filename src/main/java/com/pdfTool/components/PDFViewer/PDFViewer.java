package com.pdfTool.components.PDFViewer;

import com.pdfTool.defination.PDF;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.NonNull;

public class PDFViewer extends BorderPane {
    @FXML
    SplitPane splitPane;
    @FXML
    HBox toolBar;

    private PDFPage pdfPage;
    private PDF pdf;

    public PDFViewer(PDF pdf) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PDFViewer.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init(pdf);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void init(PDF pdf) {
        this.pdf = pdf;
        this.pdfPage = new PDFPage(this.pdf);

        PDFPageChooser pageChooser = new PDFPageChooser(this.pdfPage);
        PDFZoomTool zoomTool = new PDFZoomTool(this.pdfPage);
        PDFContent pdfContent = new PDFContent(this.pdfPage);

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        separator.setMaxHeight(30d);

        this.splitPane.getItems().addAll(pdfContent, this.pdfPage);
        Platform.runLater(() -> splitPane.setDividerPositions(0.15f));

        this.toolBar.getChildren().addAll(pageChooser, separator, zoomTool);

        this.widthProperty().addListener((observableValue, number, t1) -> this.splitPane.setDividerPositions(0.15f));
    }

    public void loadPDF(@NonNull PDF pdf) {
        this.pdf = pdf;
        this.pdfPage.loadPDF(this.pdf);
    }
}
