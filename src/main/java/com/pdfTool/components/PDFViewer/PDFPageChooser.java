package com.pdfTool.components.PDFViewer;

import com.pdfTool.components.PDFViewer.event.Parameter;
import com.pdfTool.components.PDFViewer.event.ViewerEvent;
import com.pdfTool.components.PDFViewer.event.ViewerEventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class PDFPageChooser extends HBox {
    // Nodes
    @FXML
    Label label;
    @FXML
    TextField textField;

    private PDFPage pdfPage = null;

    public PDFPageChooser(PDFPage pdfPage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PDFPageChooser.fxml"));
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
        this.pdfPage.addEventHandler(ViewerEvent.VIEWER_EVENT_TYPE, new ViewerEventHandler() {
            @Override
            public void onViewerEvent(Parameter parameter) {
                switch (parameter) {
                    case RENDER -> textField.setText(Integer.toString(PDFPageChooser.this.pdfPage.getCurrentpage() + 1));
                    case PDF_LOADED -> {
                        textField.setText(Integer.toString(PDFPageChooser.this.pdfPage.getCurrentpage() + 1));
                        label.setText(" / " + PDFPageChooser.this.pdfPage.getPdf().getNumberOfPages());
                    }
                }
            }
        });

        //Label
        this.label.setText(" / " + this.pdfPage.getPdf().getNumberOfPages());
        this.textField.setText(Integer.toString(this.pdfPage.getCurrentpage() + 1));
        this.textField.setOnAction(event -> this.pdfPage.loadPage(Integer.parseInt(this.textField.getText()) - 1));
    }
}
