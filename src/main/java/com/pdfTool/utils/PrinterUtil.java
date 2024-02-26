package com.pdfTool.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

import javax.print.PrintService;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public final class PrinterUtil {
    public static List<PrintService> getPrintServices() {
        return List.of(PrinterJob.lookupPrintServices());
    }

    public static void print(PrintService printService, List<File> files, int copies) throws PrinterException, IOException {
        for(File file:files) {
            PDDocument document = PDDocument.load(file);
            PrinterJob job = PrinterJob.getPrinterJob();
            PDFPrintable printable = new PDFPrintable(document, Scaling.ACTUAL_SIZE);
            job.setPrintable(printable);
            for (int i = 0; i < copies; i++) {
                job.print();
            }
            document.close();
        }
    }
}
