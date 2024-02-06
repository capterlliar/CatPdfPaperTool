package com.pdfTool.utils;

import com.pdfTool.defination.Paper;

import java.io.File;
import java.util.List;

public final class FileUtil {
    public static String getFileDirectory(File file) {
        String path = file.getPath();
        if(file.isDirectory()){
            return path;
        }
        else {
            int pos = path.lastIndexOf(File.separator);
            return path.substring(0,pos);
        }
    }
    public static List<Paper> filesToPapers(List<File> files) {
        return files.stream().map(Paper::new).toList();
    }
    public static boolean rename(File oldFile, File newFile) {
        if(oldFile.exists()) {
            return oldFile.renameTo(newFile);
        }
        return false;
    }
    public static boolean checkAndCreateDir(String dir) {
        if(dir.trim().equals("")) return false;
        File file = new File(dir);
        if(!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    public static boolean isFileNameValid(String name){
        if(name.trim().equals("")) return false;
        if (name.length() > 255){
            return false;
        } else {
            return name.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
        }
    }
}
