package com.example.scanner.impl;

import com.example.scanner.ImageScanner;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.tesseract;

import java.io.File;
import java.net.URL;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class ImageScannerImpl implements ImageScanner {

    @Override
    public IplImage getIplImage(String imagePath) {
        final File imageFile = new File(imagePath);
        final String imgFilePath = imageFile.getAbsolutePath();
        System.out.println(imgFilePath);
        IplImage recipeImage = cvLoadImage(imgFilePath);
        return recipeImage;
    }

    private IplImage cleanImageSmoothingForOCR(IplImage iplImage) {
        IplImage destImage = cvCreateImage(cvGetSize(iplImage), IPL_DEPTH_8U, 1);
        cvCvtColor(iplImage, destImage, CV_BGR2GRAY);
        cvSmooth(destImage, destImage, CV_MEDIAN, 3, 0, 0, 0);
        cvThreshold(destImage, destImage, 0, 255, CV_THRESH_OTSU);
        return destImage;
    }


}
