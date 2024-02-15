package com.pdfTool.utils;

import com.pdfTool.defination.Paper;
import javafx.util.Pair;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PDFUtil {
    private static Splitter splitter = null;
    public static void setNewName(Paper paper) {
        //test
        paper.setOptions(Arrays.asList("opt1","opt2","opt3","opt4"));
    }

    public static void mergeFiles(String destFilename, List<File> oldFiles) throws IOException {
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        pdfMerger.setDestinationFileName(destFilename);
        for (File file: oldFiles) {
            pdfMerger.addSource(file);
        }
        //TODO: more detailed setting?
        pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
    }

    public static List<File> splitAsMutiFiles(List<Pair<Integer, Integer>> pages, File file, String dest) throws  IOException {
        if(splitter == null) {
            splitter = new Splitter();
        }
        String filename = file.getName();
        filename = filename.substring(0, filename.length() - 4);

        List<File> files = new ArrayList<>();
        PDDocument document = PDDocument.load(file);
        for(Pair<Integer, Integer> pair:pages) {
            int start = pair.getKey();
            int end = pair.getValue();

            splitter.setStartPage(start);
            splitter.setSplitAtPage(end - start + 1);
            splitter.setEndPage(end);
            List<PDDocument> splittedPages = splitter.split(document);

            for(PDDocument pdDocument:splittedPages) {
                String newFile = dest + filename + start + "-" + end + ".pdf";
                pdDocument.save(newFile);
                pdDocument.close();
                files.add(new File(newFile));
            }
        }
        document.close();
        return files;
    }
}
