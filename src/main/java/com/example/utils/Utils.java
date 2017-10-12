package com.example.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static com.itextpdf.text.html.HtmlTags.FONT;

public class Utils {

    private static final String EXTENSION = ".pdf";
    private static final String PDF_DIRECTORY = "src/main/resources/pdf/";

    public static void createPdfFile(String fileName, String content) {
        Document document = new Document();
        try {
            Font font = FontFactory.getFont(FONT, "UTF8", BaseFont.EMBEDDED);
            FontFactory.setFontImp(new FontFactoryImp());
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PDF_DIRECTORY + fileName + EXTENSION));
            document.open();
            document.add(new Paragraph(content, font));
            document.close();
            writer.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
