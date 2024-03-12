package com.pdfTool.components.PDFViewer;

import com.pdfTool.components.PDFViewer.event.Parameter;
import com.pdfTool.components.PDFViewer.event.ViewerEvent;
import com.pdfTool.components.PDFViewer.event.ViewerEventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;

import java.io.IOException;
import java.util.ArrayList;

@Log4j
public class PDFContent extends Pane {
    private PDFPage pdfPage = null;
    @FXML
    TreeView<Hyperlink> treeView;
    private ArrayList<TreeItem<Hyperlink>> contents;

    public PDFContent(PDFPage pdfPage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PDFContent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init(pdfPage);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void init(PDFPage pdfPage) throws IOException {
        this.pdfPage = pdfPage;

        this.pdfPage.addEventHandler(ViewerEvent.VIEWER_EVENT_TYPE, new ViewerEventHandler() {
            @Override
            public void onViewerEvent(@NonNull Parameter parameter) {
                if (parameter == Parameter.PDF_LOADED) {
                    try {
                        createTreeView();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        });
        this.widthProperty().addListener((observableValue, oldNumber, newNumber) ->
                this.treeView.setPrefWidth(newNumber.doubleValue()));
        this.heightProperty().addListener((observableValue, oldNumber, newNumber) ->
                this.treeView.setPrefHeight(newNumber.doubleValue()));
        createTreeView();
    }

    private void createTreeView() throws IOException {
        this.contents = new ArrayList<>();
        PDDocumentOutline outline = this.pdfPage.getPdf().getDocument().getDocumentCatalog().getDocumentOutline();
        if (outline != null) {
            generateTree(outline, null);
        }

        TreeItem<Hyperlink> root = new TreeItem<>(new Hyperlink());
        for (TreeItem<Hyperlink> treeItem : this.contents) {
            root.getChildren().add(treeItem);
        }
        this.treeView.setRoot(root);
    }

    private void generateTree(PDOutlineNode outline, TreeItem<Hyperlink> parent) throws IOException {
        PDOutlineItem currentItem = outline.getFirstChild();
        while (currentItem != null) {
            int pageNumber = 1;
            PDPageTree pages = this.pdfPage.getPdf().getDocument().getDocumentCatalog().getPages();
            for (PDPage page : pages) {
                if (page.equals(currentItem.findDestinationPage(this.pdfPage.getPdf().getDocument()))) {
                    break;
                }
                pageNumber++;
            }

            Hyperlink hyperlink = new Hyperlink();
            hyperlink.setText(currentItem.getTitle());
            int finalNumber = pageNumber - 1;
            hyperlink.setOnAction(actionEvent -> pdfPage.loadPage(finalNumber));
            TreeItem<Hyperlink> treeItem = new TreeItem<>();
            treeItem.setExpanded(true);
            treeItem.setValue(hyperlink);

            if (parent == null) {
                this.contents.add(treeItem);
            } else {
                parent.getChildren().add(treeItem);
            }

            generateTree(currentItem, treeItem);
            currentItem = currentItem.getNextSibling();
        }
    }
}
