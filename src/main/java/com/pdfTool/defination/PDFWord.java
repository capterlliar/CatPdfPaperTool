package com.pdfTool.defination;

import lombok.Getter;

public class PDFWord {
    @Getter
    String word;
    @Getter
    Float maxFontSize;

    public PDFWord(String character, float fontSize) {
        this.word = character;
        this.maxFontSize = fontSize;
    }
}
