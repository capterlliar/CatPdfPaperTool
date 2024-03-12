package com.pdfTool.services;

import com.pdfTool.components.FilenameEditor;
import javafx.concurrent.Task;

import java.util.List;

public class RenameTask extends Task<Void> {
    List<FilenameEditor> filenameEditors;
    public RenameTask(List<FilenameEditor> filenameEditors) {
        this.filenameEditors = filenameEditors;
    }

    private void rename() {
        for(FilenameEditor filenameEditor:this.filenameEditors) {
            filenameEditor.rename();
        }
    }

    @Override
    protected Void call() {
        rename();
        return null;
    }
}
