package com.pdfTool.utils;

import com.pdfTool.defination.ExportItem;
import com.pdfTool.defination.ExportType;
import javafx.concurrent.Task;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
            fileList.addAll(splittedFiles);
        }
        String newPath = FileUtil.getFileListName(this.directory, fileList);
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
                String newFilename = FileUtil.getUniqueFilename(this.directory, filename);
                PDFUtil.mergeFiles(newFilename, splittedFiles);
                splittedFiles.forEach(File::delete);
            }
        }
    }
    private void imageIntoMutiDirs() throws IOException {
        for(ExportItem exportItem:this.exportItems) {
            File file = exportItem.getFile();
            List<Pair<Integer, Integer>> pages = exportItem.getSelectedPages();
            String tempDir = this.directory + FileUtil.getPDFFilename(file) + "/";
            PDFUtil.getImages(pages, file, tempDir);
        }
    }
    private void imageIntoOneDir() throws IOException {
        for(ExportItem exportItem:this.exportItems) {
            File file = exportItem.getFile();
            List<Pair<Integer, Integer>> pages = exportItem.getSelectedPages();
            PDFUtil.getImages(pages, file, this.directory);
        }
    }
    private void textIntoMutiFiles() throws IOException {

    }
    private void textIntoOneFile() throws IOException {

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
