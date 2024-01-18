package com.pdfTool;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class MenuViewController extends HBox {
    public MenuViewController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/MenuView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    protected void importPaper(){
        System.out.println("clicked.");
    }

    @FXML
    protected void mergePDF(){
        System.out.println("clicked.");
    }

    @FXML
    protected void splitPDF(){
        System.out.println("clicked.");
    }

    @FXML
    protected void print(){
        System.out.println("clicked.");
    }

    @FXML
    protected void getPicture(){
        System.out.println("clicked.");
    }

    @FXML
    protected void getText(){
        System.out.println("clicked.");
    }
}
