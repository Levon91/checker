package com.example.scanner;

import org.bytedeco.javacpp.opencv_core;

public interface ImageScanner {

    opencv_core.IplImage getIplImage(final String imagePath);
}
