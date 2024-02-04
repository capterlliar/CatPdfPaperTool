package com.pdfTool.utils;

import com.pdfTool.defination.Paper;

import java.util.Arrays;
import java.util.List;

public final class PDFUtil {

    public static void setNewName(Paper paper) {
        //test
        paper.setNewname("testnewname");
        paper.setOptions(Arrays.asList("opt1","opt2","opt3"));
    }
}
