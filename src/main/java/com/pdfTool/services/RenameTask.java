package com.pdfTool.services;

import com.pdfTool.components.FilenameEditorController;
import javafx.concurrent.Task;

import java.util.List;

public class RenameTask extends Task<Void> {
    List<FilenameEditorController> filenameEditors;
    public RenameTask(List<FilenameEditorController> filenameEditors) {
        this.filenameEditors = filenameEditors;
    }

    private void rename() {
        for(FilenameEditorController filenameEditor:this.filenameEditors) {
            filenameEditor.rename();
        }
    }

    @Override
    protected Void call() throws Exception {
        rename();
        return null;
    }
}
