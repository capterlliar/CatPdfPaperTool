package com.pdfTool.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.NonNull;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * @author Patr1ick
 */
public class ImageUtil {
    public static Image convertToFXImage(@NonNull BufferedImage img) {
        return SwingFXUtils.toFXImage(img, null);
    }

    public static BufferedImage scaleBufferedImage(BufferedImage img, float scaleFactor) {
        BufferedImage scaledImage = new BufferedImage((int) (img.getWidth() * scaleFactor), (int) (img.getHeight() * scaleFactor), BufferedImage.TYPE_INT_ARGB);
        AffineTransform transform = new AffineTransform();
        transform.scale(scaleFactor, scaleFactor);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        op.filter(img, scaledImage);
        return scaledImage;
    }

}
