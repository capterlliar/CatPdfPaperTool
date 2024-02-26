package com.pdfTool.services;

import com.pdfTool.utils.PDFUtil;
import com.pdfTool.utils.PrinterUtil;
import javafx.concurrent.Task;

import javax.print.PrintService;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PrintFileTask extends Task<Void> {
    List<File> files;
    PrintService printService;
    int copy;
    public PrintFileTask(List<File> files, PrintService printService, int copy) {
        this.files = files;
        this.printService = printService;
        this.copy = copy;
    }
    private void print() throws IOException, PrinterException {
        PrinterUtil.print(printService, this.files, copy);
    }
    @Override
    protected Void call() throws Exception {
        this.print();
        return null;
    }
}
