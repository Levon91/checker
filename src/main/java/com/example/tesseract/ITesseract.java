package com.example.tesseract;

import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface ITesseract {

    String doOCR(BufferedImage bi) throws TesseractException;

    String doOCR(BufferedImage bi, Rectangle rect) throws TesseractException;
}
