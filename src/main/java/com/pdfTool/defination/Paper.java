package com.pdfTool.defination;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Paper {
    @Getter
    @Setter
    int id;
    @Getter
    String path;
    @Getter
    String filename;
    String newname;
    ArrayList<String> alternatives;
    public Paper(String path, String filename) {
        this.path=path;
        this.filename=filename;
        this.newname=null;
        this.alternatives=null;
    }

    public String getDisplayedName() {
        return newname == null ? filename : newname;
    }
}
