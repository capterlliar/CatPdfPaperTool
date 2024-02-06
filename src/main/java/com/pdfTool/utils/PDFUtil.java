package com.pdfTool.utils;

import com.pdfTool.defination.Paper;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class PDFUtil {
    private static PDFMergerUtility pdfMerger = null;
    public static void setNewName(Paper paper) {
        //test
        paper.setOptions(Arrays.asList("opt1","opt2","opt3","opt4"));
    }

    public static void mergeFiles(String dest, List<File> oldFiles) throws IOException {
        if(pdfMerger == null) {
            pdfMerger = new PDFMergerUtility();
        }
        pdfMerger.setDestinationFileName(dest);
        for (File file: oldFiles) {
            pdfMerger.addSource(file);
        }
        //TODO: more detailed setting?
        pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
    }
}
