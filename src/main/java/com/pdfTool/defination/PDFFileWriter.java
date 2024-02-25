package com.pdfTool.defination;

import java.io.FileWriter;
import java.io.IOException;

public class PDFFileWriter extends FileWriter {
    int cnt = 0;

    public PDFFileWriter(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public void write(String str) throws IOException {
        if(str.equals("")) {
            cnt++;
            if(cnt == 2) {
                str += "\r\n";
                cnt = 0;
            }
        }
        else {
            cnt = 0;
            if (str.equals("\r\n") || str.equals("\r") || str.equals("\n")) {
                str = " ";
            }
        }
        write(str, 0, str.length());
    }
}
