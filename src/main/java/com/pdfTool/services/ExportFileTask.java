package com.pdfTool.services;

import com.pdfTool.defination.ExportItem;
import com.pdfTool.defination.ExportType;
import com.pdfTool.utils.FileUtil;
import com.pdfTool.utils.PDFUtil;
import javafx.concurrent.Task;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExportFileTask extends Task<Void> {
    List<ExportItem> exportItems;
    String directory;
    ExportType exportType;
    boolean intoOne;
    public ExportFileTask(List<ExportItem> exportItems, ExportType exportType, String directory, boolean intoOne) {
        this.exportItems = exportItems;
        this.directory = directory;
        this.exportType = exportType;
        this.intoOne = intoOne;
    }
    private void filesAsOneFile() throws IOException {
        List<File> fileList = new ArrayList<>();
        String tempDir = Objects.requireNonNull(this.getClass().getResource("/com/pdfTool/testfiles")).getPath();

        for(ExportItem exportItem:this.exportItems) {
            File file = exportItem.getFile();
            List<Pair<Integer, Integer>> pages = exportItem.getSelectedPages();
            if(pages.isEmpty()) {
                fileList.add(file);
                continue;
            }
            List<File> splittedFiles = PDFUtil.splitAsMutiFiles(pages, file, tempDir);
            this.updateMessage(file.getName());
            fileList.addAll(splittedFiles);
            if(this.isCancelled()) return;
        }
        String newPath = FileUtil.getFileListName(this.directory, fileList, ".pdf");
        if(newPath == null) return;
        PDFUtil.mergeFiles(newPath, fileList);
        fileList.forEach(File::delete);
    }
    private void filesAsMutiFiles() throws IOException {
        for(ExportItem exportItem:this.exportItems) {
            File file = exportItem.getFile();
            List<Pair<Integer, Integer>> pages = exportItem.getSelectedPages();
            List<File> splittedFiles = PDFUtil.splitAsMutiFiles(pages, file, this.directory);
            if(!exportItem.isExportAsMultiFiles()) {
                String filename = FileUtil.getPDFFilename(file);
                String newFilename = FileUtil.getUniqueFilename(this.directory, filename, ".pdf");
                PDFUtil.mergeFiles(newFilename, splittedFiles);
                splittedFiles.forEach(File::delete);
            }
            this.updateMessage(file.getName());
            if(this.isCancelled()) return;
        }
    }
    private void imageIntoMutiDirs() throws IOException {
        for(ExportItem exportItem:this.exportItems) {
            File file = exportItem.getFile();
            List<Pair<Integer, Integer>> pages = exportItem.getSelectedPages();
            String tempDir = this.directory + FileUtil.getPDFFilename(file) + "/";
            File dir = new File(tempDir);
            if(!dir.exists()) dir.mkdirs();
            PDFUtil.getImages(pages, file, tempDir);
            this.updateMessage(file.getName());
            if(this.isCancelled()) return;
        }
    }
    private void imageIntoOneDir() throws IOException {
        for(ExportItem exportItem:this.exportItems) {
            File file = exportItem.getFile();
            List<Pair<Integer, Integer>> pages = exportItem.getSelectedPages();
            PDFUtil.getImages(pages, file, this.directory);
            this.updateMessage(file.getName());
            if(this.isCancelled()) return;
        }
    }
    private void textIntoMutiFiles() throws IOException {
        for(ExportItem exportItem:this.exportItems) {
            File file = exportItem.getFile();
            List<Pair<Integer, Integer>> pages = exportItem.getSelectedPages();
            if(exportItem.isExportAsMultiFiles()) {
                PDFUtil.getTextAsMutiFiles(pages, file, this.directory);
            }
            else {
                PDFUtil.getTextAsOneFile(pages, file, this.directory);
            }
            this.updateMessage(file.getName());
            if(this.isCancelled()) return;
        }
    }
    private void textIntoOneFile() throws IOException {
        List<File> fileList = this.exportItems.stream().map(ExportItem::getFile).toList();
        String filename = FileUtil.getFileListName(this.directory, fileList, ".txt");
        if(filename == null) return;
        Writer output = new PDFFileWriter(filename);
        for(ExportItem exportItem:this.exportItems) {
            File file = exportItem.getFile();
            List<Pair<Integer, Integer>> pages = exportItem.getSelectedPages();
            PDFUtil.appendText(pages, file, output);
            this.updateMessage(file.getName());
            if(this.isCancelled()) return;
        }
        output.close();
    }
    @Override
    protected Void call() throws Exception {
        switch (this.exportType) {
            case SPLIT -> {
                if (this.intoOne) this.filesAsOneFile();
                else this.filesAsMutiFiles();
            }
            case IMAGE -> {
                if (this.intoOne) this.imageIntoOneDir();
                else this.imageIntoMutiDirs();
            }
            case TEXT -> {
                if (this.intoOne) this.textIntoOneFile();
                else this.textIntoMutiFiles();
            }
        }
        return null;
    }
}
