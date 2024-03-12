package com.pdfTool.components.PDFViewer.event;

import javafx.event.EventHandler;
import lombok.NonNull;

public abstract class ViewerEventHandler implements EventHandler<ViewerEvent> {
    public abstract void onViewerEvent(@NonNull Parameter parameter);
    @Override
    public void handle(ViewerEvent event) {
        event.invokeHandler(this);
    }
}
