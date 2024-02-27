package com.pdfTool.components;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.io.File;

public class FileItemController extends Label {
    @Getter
    File file;
    public FileItemController(File file) {
        this.file = file;
        this.setWrapText(true);
        this.setStyle("-fx-text-overrun: ellipsis;");
        this.setText(file.getName());
    }
    public void setFilenameColor(String color) {
        this.setTextFill(Color.valueOf(color));
    }
}
