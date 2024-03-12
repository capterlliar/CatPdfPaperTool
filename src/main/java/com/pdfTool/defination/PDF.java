package com.pdfTool.defination;

import com.pdfTool.components.PDFViewer.PDFResourceCache;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Log4j
public class PDF {
    @Getter
    private PDDocument document;
    private PDFRenderer renderer;

    public PDF(@NonNull File pdf) throws IOException {
        loadPDF(pdf);
    }

    public void loadPDF(@NonNull File pdf) throws IOException {
        this.document = PDDocument.load(pdf, MemoryUsageSetting.setupTempFileOnly());
        this.document.setResourceCache(new PDFResourceCache());
        this.renderer = new PDFRenderer(this.document);
    }

    public BufferedImage renderPage(int pageNumber, int dpi) {
        try {
            if (pageNumber <= getNumberOfPages()) {
                BufferedImage currentImage = renderer.renderImageWithDPI(pageNumber, dpi);
                System.gc();
                return currentImage;
            } else return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    public int getNumberOfPages() {
        return document.getNumberOfPages();
    }
}
