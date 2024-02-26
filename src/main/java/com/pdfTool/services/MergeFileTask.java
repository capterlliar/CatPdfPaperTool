package com.pdfTool.services;

import com.pdfTool.utils.PDFUtil;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MergeFileTask extends Task<Void> {
    String directory;
    List<File> files;
    public MergeFileTask(String directory, List<File> files) {
        this.directory = directory;
        this.files = files;
    }
    private void merge() throws IOException {
        PDFUtil.mergeFiles(this.directory, this.files);
    }
    @Override
    protected Void call() throws Exception {
        this.merge();
        return null;
    }
}
