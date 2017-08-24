package com.example.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept;
import org.bytedeco.javacpp.tesseract;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;
import static org.junit.Assert.assertTrue;

public class ImageUtils {

    public static String getDigitsFromImage(String imagePath) {
        BytePointer outText;

        tesseract.TessBaseAPI api = new tesseract.TessBaseAPI();
        // Initialize tesseract-ocr with English, without specifying tessdata path
        if (api.Init(".", "ENG") != 0) {
            System.err.println("Could not initialize tesseract.");
            System.exit(1);
        }

        // Open input image with leptonica library
        lept.PIX image = pixRead(imagePath);
        api.SetImage(image);
        // Get OCR result
        outText = api.GetUTF8Text();
        String string = outText.getString();
        assertTrue(!string.isEmpty());
        System.out.println("OCR output:\n" + string);

        // Destroy used object and release memory
        api.End();
        outText.deallocate();
        pixDestroy(image);

        return string;
    }

    public static File grayScaleImage(File image) throws IOException {
        BufferedImage master;
        BufferedImage grayScale;
        File output;

        master = ImageIO.read(new File(image.getAbsolutePath()));
        grayScale = changeColor(master, ColorSpace.CS_LINEAR_RGB, ColorSpace.CS_GRAY);

        fillPixels(grayScale);
//        fillPixels2(grayScale);
        output = new File(image.getAbsolutePath());
        ImageIO.write(grayScale, "png", output);
        return output;
    }

    public static BufferedImage getScaledInstance(BufferedImage image, int targetWidth, int targetHeight) {
        int type = (image.getTransparency() == Transparency.OPAQUE)
                ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage tmp = new BufferedImage(targetWidth, targetHeight, type);
        Graphics2D g2 = tmp.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(image, 0, 0, targetWidth, targetHeight, null);
        g2.dispose();
        return tmp;
    }

    public static  void fillPixels(BufferedImage grayScale) {
        Raster data = grayScale.getData();
        int width = grayScale.getWidth();
        int height = grayScale.getHeight();
        for (int i = 0; i < width - 1; i++) {
            for (int j = 0; j < height - 1; j++) {
                for (int k = 0; k < grayScale.getRaster().getNumBands(); k++) {
                    final int value = grayScale.getRaster().getSample(i, j, k);
                    if (value != 0) {
                        if (j < width) {
                            final int nextValue = grayScale.getRaster().getSample(i, j, k);
                            if (nextValue == 0 /*& prev == 0*/) {
                                grayScale.setRGB(i, j, Color.BLACK.getRGB());
                            }
                        }
//                    int prev = grayScale.getRGB(i, j - 1);
                    }
                }
            }
        }
    }

    public static  void fillPixels2(BufferedImage grayScale) {
        byte[] pixels = ((DataBufferByte) grayScale.getRaster().getDataBuffer()).getData();

        for (int i = 0; i < pixels.length - 1; i++) {
            System.out.println(pixels[i]);
        }
    }

    public static BufferedImage changeColor(BufferedImage image, int srcColor, int replaceColor) {
        BufferedImage destImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g = destImage.createGraphics();
        g.drawImage(image, null, 0, 0);
        g.dispose();

        for (int width = 0; width < image.getWidth(); width++) {
            for (int height = 0; height < image.getHeight(); height++) {

                if (destImage.getRGB(width, height) == srcColor) {
                    destImage.setRGB(width, height, replaceColor);
                }
            }
        }
        return destImage;
    }

    public String getStringFromImage(final String imageFilePath) {
        try {
            final URL tessDataResource = getClass().getResource("/");
            final File tessFolder = new File(tessDataResource.toURI());
            final String tessFolderPath = tessFolder.getAbsolutePath();
            System.out.println(tessFolderPath);
            BytePointer outText;
            tesseract.TessBaseAPI api = new tesseract.TessBaseAPI();
            api.SetVariable("tessedit_char_whitelist", "0123456789");
//            Initializeing api
            if (api.Init(tessFolderPath, "spa") != 0) {
                System.err.println("ERROR");
            }

            lept.PIX image = pixRead(imageFilePath);
            api.SetImage(image);
            outText = api.GetUTF8Text();
            String result = outText.getString();

            api.End();
            outText.deallocate();
            pixDestroy(image);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static  String processImage(ITesseract instance, File of) {
        String output = null;
        try {
            output = instance.doOCR(of);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return output;
    }
}
