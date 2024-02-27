package com.pdfTool.utils;

import com.pdfTool.defination.RenameItem;
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
    public static List<RenameItem> filesToPapers(List<File> files) {
        return files.stream().map(RenameItem::new).toList();
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
            return name.matches("[^\\\\s\\\\\\\\/:\\\\*\\\\?\\\\\\\"<>\\\\|](\\\\x20|[^\\\\s\\\\\\\\/:\\\\*\\\\?\\\\\\\"<>\\\\|])*[^\\\\s\\\\\\\\/:\\\\*\\\\?\\\\\\\"<>\\\\|\\\\.]$");
        }
    }

    public static String getUniqueFilename(String dest, String filenameWithoutSuffix, String suffix) {
        if(!dest.endsWith(File.separator)) dest += File.separator;
        if(filenameWithoutSuffix.endsWith(suffix)) {
            filenameWithoutSuffix = filenameWithoutSuffix
                    .substring(0, filenameWithoutSuffix.length() - suffix.length());
        }
        if(!(new File(dest + filenameWithoutSuffix + suffix).exists())){
            return dest + filenameWithoutSuffix + suffix;
        }

        int cnt=1;
        while (new File(dest + filenameWithoutSuffix + "(" + cnt + ")" + suffix).exists()) {
            cnt++;
        }
        return dest + filenameWithoutSuffix + "(" + cnt + ")" + suffix;
    }

    public static String getPDFFilename(File file) {
        String filename = file.getName();
        filename = filename.substring(0, filename.length() - 4);
        return filename;
    }

    public static List<File> GetDirFiles(File dir) {
        File[] fileList = dir.listFiles();
        if(fileList == null) return null;

        List<File> output = new ArrayList<>();
        for(File file:fileList) {
            if(file.isDirectory()) {
                List<File> res = GetDirFiles(file);
                if(res != null) output.addAll(res);
            }
            else if(file.isFile() && file.getName().endsWith(".pdf")) {
                output.add(file);
            }
        }
        return output;
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

    public static String getFileListName(String dest, List<File> fileList, String suffix) {
        if(fileList.isEmpty()) return null;
        String firstFilename = fileList.get(0).getName();
        String filename = firstFilename.substring(0, Math.min(10,firstFilename.length()))+"...等"+fileList.size()+"个文件";
        return FileUtil.getUniqueFilename(dest, filename, suffix);
    }

    public static String getSplittedFilename(String dest, String filename, int start, int end, String suffix) {
        return FileUtil.getUniqueFilename(dest, filename + start + "-" + end, suffix);
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
