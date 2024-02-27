package com.pdfTool;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class AppRun extends Application {
    @Override
    public void start(Stage primaryStage) {
        Parent root = new PrimaryViewController();
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("PDF论文信息提取器");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pdfTool/images/pdf.png")));
        Scene mainScene = new Scene(root);
        mainScene.setRoot(root);

//        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> Platform.exit());
    }

    public static void main(String[] args){
        Logger.getLogger("org.apache.pdfbox").setLevel(Level.OFF);
        Logger.getLogger("org.apache.fontbox").setLevel(Level.OFF);
        Application.launch(args);
    }
}
