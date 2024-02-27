package com.pdfTool.utils;

import com.pdfTool.services.PDFFileWriter;
import com.pdfTool.services.PDFTitleFilter;
import com.pdfTool.defination.RenameItem;
import javafx.util.Pair;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public final class PDFUtil {
    private static Splitter splitter = null;
    private static PDFTextStripper stripper = null;
    public static void setNewName(RenameItem renameItem) throws IOException {
        PDFTitleFilter titleFilter = new PDFTitleFilter();
        PDDocument document = PDDocument.load(renameItem.getFile());
        titleFilter.setSortByPosition(true);
        titleFilter.setStartPage(0);
        titleFilter.setEndPage(1);
        titleFilter.getText(document);

        List<String> options = new ArrayList<>(titleFilter.getTitle());
        String title = document.getDocumentInformation().getTitle();
        if(title != null){
            if(!title.trim().equals("")) options.add(title);
        }
        renameItem.setOptions(options);

        document.close();
    }

    public static void mergeFiles(String destFilename, List<File> oldFiles) throws IOException {
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        pdfMerger.setDestinationFileName(destFilename);
        for (File file: oldFiles) {
            pdfMerger.addSource(file);
        }
        pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
    }

    public static List<File> splitAsMutiFiles(List<Pair<Integer, Integer>> pages, File file, String destDir) throws IOException {
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
                String newFile = FileUtil.getSplittedFilename(destDir, filename, start, end, ".pdf");
                pdDocument.save(newFile);
                pdDocument.close();
                files.add(new File(newFile));
            }
        }
        document.close();
        return files;
    }

    public static List<RenderedImage> getImagesFromResources(PDResources resources) throws IOException {
        List<RenderedImage> images = new ArrayList<>();
        for (COSName xObjectName : resources.getXObjectNames()) {
            PDXObject xObject = resources.getXObject(xObjectName);
            if (xObject instanceof PDImageXObject) {
                images.add(((PDImageXObject) xObject).getImage());
            } else if (xObject instanceof PDFormXObject) {
                images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
            }
        }
        return images;
    }
    public static void getImages(List<Pair<Integer, Integer>> pages, File file, String dest) throws IOException {
        PDDocument document = PDDocument.load(file);
        String filename = FileUtil.getPDFFilename(file);

        if(pages.isEmpty()) {
            int end = document.getNumberOfPages();
            pages.add(new Pair<>(1, end));
        }

        for(Pair<Integer, Integer> pair:pages) {
            int start = pair.getKey();
            int end = pair.getValue();

            for(int i=start;i<=end;i++) {
                PDPage page = document.getPage(i-1);
                PDResources resources = page.getResources();
                List<RenderedImage> images = getImagesFromResources(resources);
                int cnt = 1;
                for(RenderedImage image:images) {
                    String newFile = FileUtil.getUniqueFilename(dest,  filename + " p"+ i + "(" + cnt + ")", ".png");
                    File tempfile = new File(newFile);
                    tempfile.createNewFile();
                    ImageIO.write(image, "png", tempfile);
                    cnt++;
                }
            }
        }
        document.close();
    }

    public static void getTextAsMutiFiles(List<Pair<Integer, Integer>> pages, File file, String dest) throws IOException {
        PDDocument document = PDDocument.load(file);
        String filename = FileUtil.getPDFFilename(file);

        if(stripper == null) {
            stripper = new PDFTextStripper();
        }

        if(pages.isEmpty()) {
            int end = document.getNumberOfPages();
            pages.add(new Pair<>(1, end));
        }

        for(Pair<Integer, Integer> pair:pages) {
            int start = pair.getKey();
            int end = pair.getValue();

            String newfile = dest + filename + start + "-" + end + ".txt";
            Writer output = new PDFFileWriter(newfile);

            stripper.setStartPage(start);
            stripper.setEndPage(end);
            stripper.writeText(document, output);
            output.close();
        }
        document.close();
    }
    public static void getTextAsOneFile(List<Pair<Integer, Integer>> pages, File file, String dest) throws IOException {
        PDDocument document = PDDocument.load(file);
        String filename = FileUtil.getPDFFilename(file);

        if(stripper == null) {
            stripper = new PDFTextStripper();
        }

        if(pages.isEmpty()) {
            int end = document.getNumberOfPages();
            pages.add(new Pair<>(1, end));
        }

        String newfile = FileUtil.getUniqueFilename(dest, filename, ".txt");
        Writer output = new PDFFileWriter(newfile);
        for(Pair<Integer, Integer> pair:pages) {
            int start = pair.getKey();
            int end = pair.getValue();

            stripper.setStartPage(start);
            stripper.setEndPage(end);
            stripper.writeText(document, output);
        }
        output.close();
        document.close();
    }

    public static void appendText(List<Pair<Integer, Integer>> pages, File file, Writer output) throws IOException {
        PDDocument document = PDDocument.load(file);

        if(stripper == null) {
            stripper = new PDFTextStripper();
        }

        if(pages.isEmpty()) {
            int end = document.getNumberOfPages();
            pages.add(new Pair<>(1, end));
        }

        for(Pair<Integer, Integer> pair:pages) {
            int start = pair.getKey();
            int end = pair.getValue();

            stripper.setStartPage(start);
            stripper.setEndPage(end);
            stripper.writeText(document, output);
        }
        document.close();
    }
}
