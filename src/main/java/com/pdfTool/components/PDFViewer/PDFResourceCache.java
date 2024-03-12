package com.pdfTool.components.PDFViewer;

import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.DefaultResourceCache;
import org.apache.pdfbox.pdmodel.documentinterchange.markedcontent.PDPropertyList;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

public class PDFResourceCache extends DefaultResourceCache {
    public void put(COSObject indirect, PDColorSpace colorSpace) {

    }

    public void put(COSObject indirect, PDExtendedGraphicsState extGState) {

    }

    public void put(COSObject indirect, PDShading shading) {

    }

    public void put(COSObject indirect, PDAbstractPattern pattern) {

    }

    public void put(COSObject indirect, PDPropertyList propertyList) {

    }

    public void put(COSObject indirect, PDXObject xobject) {

    }
}
