package com.pdfTool.defination;

public enum ExportType {
    SPLIT{
        @Override
        public String getWindowName(){
            return "Split PDF";
        }
    },
    IMAGE{
        @Override
        public String getWindowName(){
            return "Export images";
        }
    },
    TEXT{
        @Override
        public String getWindowName(){
            return "Export text";
        }
    };
    public abstract String getWindowName();
}
