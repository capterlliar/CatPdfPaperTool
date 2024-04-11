package com.pdfTool.services;

import com.pdfTool.defination.PDFWord;
import com.pdfTool.utils.FileUtil;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.*;

/**
 * 我们假定论文标题具有以下特点：
 * 1. 字号大于正文
 * 2. 位置相对偏上
 * 3. 标题与正文不在同一行
 * 4. 标题占据连续的几行
 */

public class PDFTitleFilter extends PDFTextStripper {
    public List<PDFWord> pdfWords;
    private final int MIN_TITLE_LENTH = 2;
    private final int MAX_TITLE_LENTH = 50;

    public PDFTitleFilter() throws IOException {
        super();
        this.pdfWords = new ArrayList<>();
    }
    @Override
    protected void writeString(String string, List<TextPosition> textPositions) {
        float max_font_size = -1.0f;
        for (TextPosition textPosition : textPositions) {
            if (textPosition.getFontSizeInPt() > max_font_size &&
                    textPosition.getDir() == 0.0 &&
                    !string.equals(" ")) {
                max_font_size = textPosition.getFontSizeInPt();
            }
        }
        if (max_font_size > 0.0f) {
            this.pdfWords.add(new PDFWord(string, max_font_size, textPositions.get(0).getY()));
        }
    }

    public List<String> getTitle() {
        List<PDFWord> title = new ArrayList<>();
        Set<Float> titleFontRange = new HashSet<>(this.getTitleFontRange());
        int pos = 0;
        while(pos < this.pdfWords.size()) {
            float fontsize = this.pdfWords.get(pos).getMaxFontSize();
            if(titleFontRange.contains(fontsize)) {
                List<String> statement = this.getTitleOptionWithFontsizeAndYPos(pos, fontsize);
                String titleOption = FileUtil.cleanFileName(FileUtil.JoinString(statement, " "));
                if(!titleOption.equals("")) {
                    String[] s = titleOption.split(" ");
                    if(s.length > MIN_TITLE_LENTH && s.length <= MAX_TITLE_LENTH) {
                        title.add(new PDFWord(FileUtil.cleanFileName(titleOption), fontsize));
                    }
                }
                pos += statement.size();
            }
            else {
                pos++;
            }
        }
        return this.sortTitleOptions(title);
    }

    private List<String> sortTitleOptions(List<PDFWord> titles) {
        Set<String> options = new HashSet<>();
        options.addAll(titles.stream()
                .sorted(Comparator.comparing(PDFWord::getMaxFontSize).reversed())
                .limit(2)
                .map(PDFWord::getWord).toList());
        options.addAll(titles.stream()
                .limit(2)
                .map(PDFWord::getWord).toList());
        List<String> res = new ArrayList<>();
        for(String title: options) {
            int n = title.length()-title.replaceAll(",", "").length();
            if(n >= 5) res.add(title);
            else res.add(0, title);
        }
        return res;
    }

    private List<String> getTitleOptionWithFontsizeAndYPos(int pos, float fontsize) {
        List<String> res = new ArrayList<>();
        float yPos = -1;
        for(int i = pos;i<this.pdfWords.size();i++) {
            PDFWord pdfWord = this.pdfWords.get(i);
            float currentYPos = pdfWord.getYPos();
            if(yPos == -1) yPos = currentYPos;
            if(currentYPos == yPos) {
                res.add(pdfWord.getWord());
                continue;
            }
            if(pdfWord.getMaxFontSize() != fontsize) break;
            res.add(pdfWord.getWord());
            yPos = currentYPos;
        }
        return res;
    }

    private List<Float> getTitleFontRange() {
        List<Float> list = this.pdfWords.stream()
                .map(PDFWord::getMaxFontSize)
                .distinct()
                .sorted(Comparator.reverseOrder()).toList();
        return list.subList(0, Math.min(list.size() / 3 + 1, list.size()));
    }
}
