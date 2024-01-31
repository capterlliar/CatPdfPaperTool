package com.pdfTool.utils;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.List;

public final class FileChooserUtil {
    private static String initialDir = System.getProperty("user.home");
    private static DirectoryChooser directoryChooser = null;
    private static FileChooser fileChooser = null;

    static {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );
        directoryChooser = new DirectoryChooser();
    }

    public static List<File> getFiles(Window window) {
        fileChooser.setInitialDirectory(new File(initialDir));
        List<File> files = fileChooser.showOpenMultipleDialog(window);
        if (files!=null) {
            initialDir = FileUtil.getFileDirectory(files.get(0));
        }
        return files;
    }

    public static File getDirectory(Window window) {
        directoryChooser.setInitialDirectory(new File(initialDir));
        File dir = directoryChooser.showDialog(window);
        if(dir!=null) {
            initialDir = dir.getPath();
        }
        return dir;
    }
}
