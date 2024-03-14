package com.pdfTool.components.PDFViewer;

import com.pdfTool.components.PDFViewer.event.Parameter;
import com.pdfTool.components.PDFViewer.event.ViewerActionEvent;
import com.pdfTool.components.PDFViewer.event.ViewerEvent;
import com.pdfTool.components.PDFViewer.event.ViewerEventHandler;
import com.pdfTool.defination.PDF;
import com.pdfTool.utils.ImageUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import static javafx.scene.input.KeyCombination.CONTROL_DOWN;

@Log4j
public class PDFPage extends Pane {
    @FXML
    ScrollPane scrollPane;
    @FXML
    StackPane stackPane;
    @FXML
    ImageView imageView;

    @Getter
    private PDF pdf;

    @Getter
    private int currentpage;

    // Quality
    public static final int DEFAULT_DPI = 300;
    @Getter
    private int dpi = DEFAULT_DPI;

    // Scale
    public static final double DEFAULT_SCALE = 0.3;
    public static final double MAXSCALE = 1.0;
    public static final double MINSCALE = 0.1;
    public static final double SCALE_STEP = 0.1;
    @Getter
    private double scaleFactor = DEFAULT_SCALE;

    // KeyBinding
    @Setter
    @Getter
    private KeyCodeCombination keyCodeBegin;
    @Setter
    @Getter
    private KeyCodeCombination keyCodeEnd;

    @Setter
    @Getter
    private KeyCode keyCodeLeft;
    @Setter
    @Getter
    private KeyCode keyCodeRight;

    @Getter
    @Setter
    private double scrollspeed = 3.0d;

    public PDFPage(PDF pdf) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PDFPage.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init(pdf);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void init(PDF pdf) {
        this.pdf = pdf;

        this.initStackPane();
        this.initScrollPane();

        this.widthProperty().addListener((observableValue, oldNumber, newNumber) -> this.scrollPane.setPrefWidth(newNumber.doubleValue()));
        this.heightProperty().addListener((observableValue, oldNumber, newNumber) -> this.scrollPane.setPrefHeight(newNumber.doubleValue()));

        // KeyBindings
        // KeyBinds
        this.keyCodeEnd = new KeyCodeCombination(KeyCode.RIGHT, CONTROL_DOWN);
        this.keyCodeBegin = new KeyCodeCombination(KeyCode.LEFT, CONTROL_DOWN);
        this.keyCodeLeft = KeyCode.LEFT;
        this.keyCodeRight = KeyCode.RIGHT;

        this.setEvents();

        this.fireEvent(new ViewerActionEvent(Parameter.RENDER));
        this.requestFocus();
    }

    private void initStackPane() {
        // StackPane to center the Image
        this.stackPane.setOnScroll(scrollEvent -> {
            if (scrollEvent.isControlDown()) {
                double before = scaleFactor;
                if (scrollEvent.getDeltaY() < 0 && scaleFactor > MINSCALE) {
                    // Zoom out
                    scaleFactor -= SCALE_STEP;
                } else if (scrollEvent.getDeltaY() > 0 && scaleFactor < MAXSCALE) {
                    // Zoom in
                    scaleFactor += SCALE_STEP;
                }
                if (before != scaleFactor) {
                    this.fireEvent(new ViewerActionEvent(Parameter.RENDER));
                }
            } else {
                double deltaY = scrollEvent.getDeltaY() * scrollspeed;
                double height = this.scrollPane.getContent().getBoundsInLocal().getHeight();
                this.scrollPane.setVvalue(this.scrollPane.getVvalue() + (-deltaY / height));
            }
        });
    }

    private void initScrollPane() {
        this.scrollPane.setOnKeyPressed(keyEvent -> {
            if (keyCodeBegin.match(keyEvent)) {
                begin();
            }
            if (keyCodeEnd.match(keyEvent)) {
                end();
            }
            if (keyCodeLeft.equals(keyEvent.getCode())) {
                leftPage();
            }
            if (keyCodeRight.equals(keyEvent.getCode())) {
                rightPage();
            }
        });

        // Scrolling
        this.scrollPane.setOnScroll(scrollEvent -> {
            if (this.scrollPane.getVvalue() == this.scrollPane.getVmax()) {
                this.fireEvent(new ViewerActionEvent(Parameter.RIGHT));
            }
            if (this.scrollPane.getVvalue() == this.scrollPane.getVmin()) {
                this.fireEvent(new ViewerActionEvent(Parameter.LEFT));
            }
        });

        // Not tested!
        this.scrollPane.setOnSwipeLeft(swipeEvent -> this.fireEvent(new ViewerActionEvent(Parameter.LEFT)));

        this.scrollPane.setOnSwipeRight(swipeEvent -> this.fireEvent(new ViewerActionEvent(Parameter.LEFT)));

    }

    private void setEvents() {
        this.addEventHandler(ViewerEvent.VIEWER_EVENT_TYPE, new ViewerEventHandler() {
            @Override
            public void onViewerEvent(Parameter parameter) {
                new Thread(() ->
                        Platform.runLater(() -> {
                            switch (parameter) {
                                case LEFT -> eventLeftPage();
                                case RIGHT -> eventRightPage();
                                case BEGIN -> eventBegin();
                                case END -> eventEnd();
                                case RENDER -> eventRender();
                                default -> log.debug("Wrong parameter is given");
                            }
                        })
                ).start();

            }
        });
    }

    public void loadPDF(@NonNull PDF pdf) {
        this.pdf = pdf;
        this.currentpage = 0;
        this.fireEvent(new ViewerActionEvent(Parameter.PDF_LOADED));
        this.fireEvent(new ViewerActionEvent(Parameter.RENDER));
    }

    public void loadPage(int pageNumber) {
        if (pageNumber >= 0 && pageNumber < this.pdf.getNumberOfPages()) {
            this.currentpage = pageNumber;
            this.fireEvent(new ViewerActionEvent(Parameter.RENDER));
        } else {
            throw new IllegalArgumentException("The page number given is outside that of the PDF.");
        }
    }

    public void leftPage() {
        this.fireEvent(new ViewerActionEvent(Parameter.LEFT));
    }

    public void rightPage() {
        this.fireEvent(new ViewerActionEvent(Parameter.RIGHT));
    }

    public void begin() {
        this.fireEvent(new ViewerActionEvent(Parameter.BEGIN));
    }

    public void end() {
        this.fireEvent(new ViewerActionEvent(Parameter.END));
    }

    private void eventLeftPage() {
        if (this.currentpage > 0) {
            this.currentpage--;
            this.fireEvent(new ViewerActionEvent(Parameter.RENDER));
            this.scrollPane.setVvalue(this.scrollPane.getVmax() - 0.001);
        }
    }

    private void eventRightPage() {
        if (this.currentpage < this.pdf.getNumberOfPages() - 1) {
            this.currentpage++;
            this.fireEvent(new ViewerActionEvent(Parameter.RENDER));
            this.scrollPane.setVvalue(this.scrollPane.getVmin() + 0.001);
        }
    }

    private void eventBegin() {
        this.currentpage = 0;
        this.fireEvent(new ViewerActionEvent(Parameter.RENDER));
        this.scrollPane.setVvalue(this.scrollPane.getVmin());
    }

    private void eventEnd() {
        this.currentpage = this.pdf.getNumberOfPages() - 1;
        this.fireEvent(new ViewerActionEvent(Parameter.RENDER));
        this.scrollPane.setVvalue(this.scrollPane.getVmax());
    }

    private void eventRender() {
        if (this.currentpage >= 0 && this.currentpage < this.pdf.getNumberOfPages()) {
            BufferedImage currentImage = this.pdf.renderPage(this.currentpage, this.dpi);
            currentImage = ImageUtil.scaleBufferedImage(currentImage, (float) scaleFactor);
            this.imageView.setImage(ImageUtil.convertToFXImage(currentImage));
            System.gc();
        }
    }

    public void setScaleFactor(double scaleFactor) {
        if (scaleFactor >= MINSCALE && scaleFactor <= MAXSCALE) {
            this.scaleFactor = Double.parseDouble(new DecimalFormat("0.00").format(scaleFactor));
            this.fireEvent(new ViewerActionEvent(Parameter.RENDER));
        }
    }

    public void scaleByValue(double scaleFactor) {
        double newValue = this.scaleFactor + scaleFactor;
        newValue = Double.parseDouble(new DecimalFormat("0.00").format(newValue));
        if (newValue >= MINSCALE && newValue <= MAXSCALE) {
            this.scaleFactor = newValue;
            this.fireEvent(new ViewerActionEvent(Parameter.RENDER));
        }
    }
}
