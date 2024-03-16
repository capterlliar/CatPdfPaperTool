package com.pdfTool.menuFunctions;

import com.pdfTool.defination.ExportType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class AboutViewController extends VBox {
    Stage stage;
    public AboutViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AboutView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    public void show() {
        Scene scene = new Scene(this);

        if(stage==null) {
            stage = new Stage();
            stage.initOwner(this.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(
                    Objects.requireNonNull(
                            getClass().getResourceAsStream("/com/pdfTool/images/cat.png"))));
        }
        stage.setTitle("关于");
        stage.setScene(scene);
        stage.show();
    }
}
