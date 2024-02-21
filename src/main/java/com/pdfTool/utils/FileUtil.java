package com.pdfTool.utils;

import com.pdfTool.defination.Paper;
import javafx.util.Pair;

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

    public static String getFileListName(List<File> fileList) {
        String firstFilename = fileList.get(0).getName().split(" ")[1];
        return firstFilename.substring(0, Math.min(10,firstFilename.length()))+"...等"+fileList.size()+"个文件.pdf";
    }

    public static List<Pair<Integer, Integer>> getSelectedPages(String s) {
        List<Pair<Integer, Integer>> res = new ArrayList<>();
        //输入为空，默认导出整个文件
        if(s.trim().equals("")) return res;
        if(!s.matches("([0-9]|-|,)+")) return null;

        String[] s1 = s.split(",");
        if(s1.length == 0) return null;
        for(String s2: s1) {
            s2 = s2.trim();
            String[] s3 = s2.split("-");
            //e.g. "9"
            if(s3.length == 1){
                int i = Integer.parseInt(s3[0].trim());
                res.add(new Pair<>(i, i));
            }
            //e.g. "3-4", "-4"
            else if(s3.length == 2){
                int from = Integer.parseInt(s3[0].trim());
                int to = Integer.parseInt(s3[1].trim());
                if(from <= to) res.add(new Pair<>(from,to));
                else {
                    return null;
                }
            }
            else {
                return null;
            }
        }
        return res;
    }
}
