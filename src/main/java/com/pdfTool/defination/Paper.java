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
    public Paper(String p, String f) {
        path=p;
        filename=f;
        newname=null;
        alternatives=null;
    }
}
