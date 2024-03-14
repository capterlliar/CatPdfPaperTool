package com.pdfTool.defination;

import javafx.util.Pair;
import lombok.Getter;

import java.io.File;
import java.util.List;

public class ExportItem {
    @Getter
    File file;
    @Getter
    List<Pair<Integer, Integer>> selectedPages;
    @Getter
    boolean isExportAsMultiFiles;
    public ExportItem(File file, List<Pair<Integer, Integer>> selectedPages, boolean isExportAsMultiFiles) {
        this.file = file;
        this.selectedPages = selectedPages;
        this.isExportAsMultiFiles = isExportAsMultiFiles;
    }
}
