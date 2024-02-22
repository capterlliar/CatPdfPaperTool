package com.pdfTool.defination;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

public class RenameItem {
    @Getter
    @Setter
    int id;
    @Getter
    @Setter
    File file;
    @Setter
    @Getter
    List<String> options;
    public RenameItem(File file) {
        this.file = file;
        this.options = null;
    }
}
