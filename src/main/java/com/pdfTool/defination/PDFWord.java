package com.pdfTool.defination;

import lombok.Getter;

public class PDFWord {
    @Getter
    String word;
    @Getter
    Float maxFontSize;

    @Getter
    Float yPos;

    public PDFWord(String character, float fontSize, float yPos) {
        this.word = character;
        this.maxFontSize = fontSize;
        this.yPos = yPos;
    }

    public PDFWord(String character, float fontSize) {
        this.word = character;
        this.maxFontSize = fontSize;
    }
}
