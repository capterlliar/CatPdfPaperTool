package com.pdfTool.utils;

import com.pdfTool.defination.Paper;

import java.io.File;
import java.util.ArrayList;
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

    public static String getUniqueFilename(String dest, String oldFilename) {
        //Avoid duplicated filename.
        //We do not choose uuid as filename to keep its readability.
        String newFilename;
        int cnt = 1;
        while (new File(dest + "new" + cnt + " " + oldFilename).exists()) {
            cnt++;
        }
        newFilename = dest + "new" + cnt + " " + oldFilename;
        return newFilename;
    }

    public static String getPDFFilename(File file) {
        String filename = file.getName();
        filename = filename.substring(0, filename.length() - 4);
        return filename;
    }

    public static List<String> GetDirFiles(final String dirName) {
        final File file = new File(dirName);
        List<String> output = new ArrayList<String>();
        output = ergodic(file, output);
        return output;
    }

    private static List<String> ergodic(final File file, final List<String> resultFileName) {
        final File[] files = file.listFiles();
        if (files == null) {
            return resultFileName;
        }
        for (final File f : files) {
            if (f.isDirectory()) {
                ergodic(f, resultFileName);
            }
            else {
                resultFileName.add(f.getPath());
            }
        }
        return resultFileName;
    }

    public static String JoinString(final List<String> list, final String conjunction) {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (final String item : list) {
            if (first) first = false;
            else sb.append(conjunction);
            sb.append(item);
        }
        return sb.toString();
    }

    public static String cleanFileName(String input) {
        input = input.replace("- ", "");
        input = input.replaceAll("\\?|\u3001|\u2572|/|\\*|<|>|:", "_");
        return input.trim();
    }
}
