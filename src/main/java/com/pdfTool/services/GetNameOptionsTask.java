package com.pdfTool.services;

import com.pdfTool.components.FilenameEditor;
import javafx.concurrent.Task;

import java.util.List;

public class GetNameOptionsTask extends Task<Void> {
    List<FilenameEditor> filenameEditors;
    public GetNameOptionsTask(List<FilenameEditor> filenameEditors) {
        this.filenameEditors = filenameEditors;
    }

    private void modify() {
        for(FilenameEditor filenameEditor:this.filenameEditors) {
            filenameEditor.modify();
        }
    }
    @Override
    protected Void call() {
        modify();
        return null;
    }
}
