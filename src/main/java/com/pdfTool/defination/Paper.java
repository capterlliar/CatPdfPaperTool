package com.pdfTool.defination;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

public class Paper {
    @Getter
    @Setter
    int id;
    @Getter
    @Setter
    File file;
    @Setter
    @Getter
    List<String> options;
    public Paper(File file) {
        this.file = file;
        this.options = null;
    }
}
