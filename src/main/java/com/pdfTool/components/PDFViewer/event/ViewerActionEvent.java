package com.pdfTool.components.PDFViewer.event;

import javafx.event.EventType;
import lombok.NonNull;

public class ViewerActionEvent extends ViewerEvent {
    public static final EventType<ViewerEvent> VIEWER_ACTION_EVENT_TYPE = new EventType<>(VIEWER_EVENT_TYPE, "ViewerEvent");
    private final Parameter parameter;
    public ViewerActionEvent(@NonNull Parameter parameter) {
        super(VIEWER_ACTION_EVENT_TYPE);
        this.parameter = parameter;
    }
    @Override
    public void invokeHandler(ViewerEventHandler handler) {
        handler.onViewerEvent(parameter);
    }
}
