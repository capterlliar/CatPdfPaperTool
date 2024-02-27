package com.pdfTool.services;

import com.pdfTool.components.FilenameEditorController;
import javafx.concurrent.Task;

import java.util.List;

public class GetNameOptionsTask extends Task<Void> {
    List<FilenameEditorController> filenameEditors;
    public GetNameOptionsTask(List<FilenameEditorController> filenameEditors) {
        this.filenameEditors = filenameEditors;
    }

    private void modify() {
        for(FilenameEditorController filenameEditor:this.filenameEditors) {
            filenameEditor.modify();
        }
    }
    @Override
    protected Void call() throws Exception {
        modify();
        return null;
    }
}
