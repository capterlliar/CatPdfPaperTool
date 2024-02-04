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
    String path;
    @Getter
    String filename;
    @Setter
    @Getter
    String newname;
    @Setter
    @Getter
    List<String> options;
    public Paper(String path, String filename) {
        this.path=path;
        this.filename=filename;
        this.newname=null;
        this.options=null;
    }

    public String getDisplayedName() {
        return newname == null ? filename : newname;
    }
}
