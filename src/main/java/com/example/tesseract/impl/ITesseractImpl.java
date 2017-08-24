package com.example.tesseract.impl;

import com.example.tesseract.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoggHelper;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ITesseractImpl implements ITesseract {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(new LoggHelper().toString());

    @Override
    public String doOCR(BufferedImage bi) throws TesseractException {
        return doOCR(bi, null);
    }

    @Override
    public String doOCR(BufferedImage bi, Rectangle rect) throws TesseractException {
        try {
//            return doOCR(ImageIOHelper.getIIOImageList(bi), rect);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new TesseractException(e);
        }
        return null;
    }
}
