package com.pdfTool.utils;

import com.pdfTool.defination.Paper;
import javafx.util.Pair;

import java.io.File;
import java.util.List;

public final class FileUtil {
    public static String getDirectory(String path) {
        int pos = path.lastIndexOf(File.separator);
        return path.substring(0,pos);
    }
    public static String getFileDirectory(File file) {
        String path = file.getPath();
        if(file.isDirectory()){
            return path;
        }
        else {
            return getDirectory(path);
        }
    }
    public static List<Paper> filesToPapers(List<File> files) {
        return files.stream().map(file ->new Paper(file.getPath(), file.getName())).toList();
    }
    public static boolean rename(Pair<File, File> files) {
        if(files.getKey().exists()) {
            return files.getKey().renameTo(files.getValue());
        }
        return false;
    }
}
