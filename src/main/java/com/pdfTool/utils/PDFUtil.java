package com.pdfTool.utils;

import com.pdfTool.defination.Paper;
import javafx.util.Pair;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

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
        String filename = FileUtil.getPDFFilename(file);

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

    public static List<File> getImages(List<Pair<Integer, Integer>> pages, File file, String dest) throws IOException {
        PDDocument document = PDDocument.load(file);
        String filename = FileUtil.getPDFFilename(file);

        if(pages.isEmpty()) {
            int end = document.getNumberOfPages();
            pages.add(new Pair<>(1, end));
        }

        List<File> images = new ArrayList<>();
        for(Pair<Integer, Integer> pair:pages) {
            int start = pair.getKey();
            int end = pair.getValue();

            for(int i=start;i<=end;i++) {
                PDPage page = document.getPage(i-1);
                PDResources resources = page.getResources();
                int cnt = 1;
                for (COSName xObjectName : resources.getXObjectNames()) {
                    PDXObject xObject = resources.getXObject(xObjectName);
                    if (xObject instanceof PDImageXObject) {
                        File image = new File(dest + filename + start + "-" + end + "(" + cnt + ")" + ".png");
                        ImageIO.write(((PDImageXObject)xObject).getImage(), "png", image);
                        images.add(image);
                    }
                }
            }
        }
        document.close();
        return images;
    }
}
