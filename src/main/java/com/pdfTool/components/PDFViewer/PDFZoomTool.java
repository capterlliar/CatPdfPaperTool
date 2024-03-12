package com.pdfTool.components.PDFViewer;

import com.pdfTool.components.PDFViewer.event.Parameter;
import com.pdfTool.components.PDFViewer.event.ViewerEvent;
import com.pdfTool.components.PDFViewer.event.ViewerEventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import lombok.extern.log4j.Log4j;

import java.text.DecimalFormat;
import java.util.Objects;

@Log4j
public class PDFZoomTool extends HBox {
    @FXML
    TextField textFieldZoom;
    @FXML
    Button btnReset;
    @FXML
    Button btnZoomOut;
    @FXML
    Button btnZoomIn;
    private PDFPage pdfPage = null;

    public PDFZoomTool(PDFPage pdfPage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PDFZoomTool.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init(pdfPage);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void init(PDFPage pdfPage) {
        this.pdfPage = pdfPage;
        this.btnReset.setOnAction(actionEvent -> this.pdfPage.setScaleFactor(PDFPage.MINSCALE));
        this.btnZoomIn.setOnAction(actionEvent -> {
            if (this.pdfPage.getScaleFactor() <= PDFPage.MAXSCALE) {
                this.pdfPage.scaleByValue(PDFPage.SCALE_STEP);
            }
        });
        this.btnZoomOut.setOnAction(actionEvent -> {
            if (this.pdfPage.getScaleFactor() > PDFPage.MINSCALE) {
                this.pdfPage.scaleByValue(PDFPage.SCALE_STEP * -1);
            }
        });
        this.textFieldZoom.setText(new DecimalFormat("0").format(this.pdfPage.getScaleFactor() * 100));
        this.textFieldZoom.setOnAction(actionEvent -> {
            double value = Double.parseDouble(this.textFieldZoom.getText()) / 100;
            if (value >= PDFPage.MINSCALE && value <= PDFPage.MAXSCALE) {
                this.pdfPage.setScaleFactor(value);
            }
        });
        this.pdfPage.addEventHandler(ViewerEvent.VIEWER_EVENT_TYPE, new ViewerEventHandler() {
            @Override
            public void onViewerEvent(Parameter parameter) {
                if (Objects.requireNonNull(parameter) == Parameter.RENDER) {
                    textFieldZoom.setText(new DecimalFormat("0").format(pdfPage.getScaleFactor() * 100));
                }
            }
        });
    }
}