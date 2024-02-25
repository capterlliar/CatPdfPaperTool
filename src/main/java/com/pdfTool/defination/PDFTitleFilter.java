package com.pdfTool.defination;

import com.pdfTool.defination.PDFWord;
import com.pdfTool.utils.FileUtil;
import javafx.util.Pair;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 我们假定论文标题具有以下特点：
 * 1. 字号大于正文
 * 2. 位置相对偏上
 * 3. 标题与正文不在同一行
 * 4. 标题占据连续的几行
 */

public class PDFTitleFilter extends PDFTextStripper {
    public List<PDFWord> pdfWords;

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
            this.pdfWords.add(new PDFWord(string, max_font_size));
        }
    }

    public List<String> getTitle() {
        List<PDFWord> res = new ArrayList<>();

        Set<Float> titleFontRange = new HashSet<>(this.getTitleFontRange());
        int pos = 0;
        while(pos < this.pdfWords.size()) {
            float fontsize = this.pdfWords.get(pos).getMaxFontSize();
            if(titleFontRange.contains(fontsize)) {
                List<String> statement = this.getTitleOptionWithFontsize(pos, fontsize);
                String titleOption = FileUtil.cleanFileName(FileUtil.JoinString(statement, " "));
                if(!titleOption.equals("")) {
                    res.add(new PDFWord(FileUtil.cleanFileName(titleOption), fontsize));
                }
                pos += statement.size();
            }
            else {
                pos++;
            }
        }
        return res.stream()
                .sorted(Comparator.comparing(PDFWord::getMaxFontSize).reversed())
                .limit(3)
                .map(PDFWord::getWord).toList();
    }

    private List<String> getTitleOptionWithFontsize(int pos, float fontsize) {
        List<String> res = new ArrayList<>();
        for(int i = pos;i<this.pdfWords.size();i++) {
            PDFWord pdfWord = this.pdfWords.get(i);
            if(pdfWord.getMaxFontSize() != fontsize) break;
            res.add(pdfWord.getWord());
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
