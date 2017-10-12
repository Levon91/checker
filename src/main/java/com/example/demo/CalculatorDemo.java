package com.example.demo;

import com.example.utils.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static com.example.utils.ImageUtils.grayScaleImage;

public class CalculatorDemo {

    private static final String IMG_DIRECTORY = "src/main/resources/images/";
    private static final String IMG_EXTENSION = ".tif";

    public static void main(String[] args) {
        CalculatorDemo calculator = new CalculatorDemo();
        calculator.startShow();
    }

    private void startShow() {
//        FirefoxDriver driver;
        WebDriver driver;
//        ITesseract instance;

        try {

            driver = new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.get("http://report.appa.am/PaapBureau/Report/RequestReport03");

            driver.manage().window().maximize();

            driver.manage().timeouts().pageLoadTimeout(5L, TimeUnit.SECONDS);

            driver.findElement(By.xpath(".//*[@id='vehicleNumber']")).sendKeys("35FT311");
            wait(driver, 3, TimeUnit.SECONDS);

            driver.findElement(By.xpath(".//*[@id='requestDateTime']")).sendKeys("24.08.2017 00:00");

            wait(driver, 3, TimeUnit.SECONDS);
            driver.findElement(By.className("ui-datepicker-close")).click();

            WebElement img = driver.findElement(By.xpath("//img[contains(@title,'Փոխել')]"));

            String imgSrc = img.getAttribute("src");

            File of = createImageFile(imgSrc);

            File converted = grayScaleImage(of);
//            String digits = getDigitsFromImage(converted.getAbsolutePath());
//            System.out.println(digits);

//            BufferedImage image = ImageIO.read(of);
//            BufferedImage scaledInstance = getScaledInstance(image, image.getWidth(), image.getHeight());

//            File outFile = new File(of.getAbsolutePath());
//            ImageIO.write(scaledInstance, IMG_EXTENSION, outFile);

//            if (outFile.exists()) {
//                System.out.println("SCALED");
//            }

//            String result = getStringFromImage(of.getAbsolutePath());

//            String result;
//            instance = new Tesseract();
//            result = processImage(instance, of);

//            System.out.println(result);

            driver.findElement(By.xpath(".//*[@id='inp-captcha']")).sendKeys("");
            wait(driver, 3, TimeUnit.SECONDS);


            driver.findElement(By.xpath(".//*[@id='isAgree']")).click();
            wait(driver, 3, TimeUnit.SECONDS);

            driver.findElement(By.xpath(".//*[@id='btn-request']")).click();

            wait(driver, 5, TimeUnit.SECONDS);

            WebElement requestResult = driver.findElement(By.className("report-tbl"));

            String resultText = requestResult.getText();

            Utils.createPdfFile(String.valueOf(System.currentTimeMillis()), resultText);
            System.out.println(resultText);

            driver.quit();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile(String imgSrc) throws IOException {
        imgSrc = imgSrc.substring(imgSrc.indexOf(",") + 1, imgSrc.length());
        System.out.println(imgSrc);

        String fileName = String.valueOf(System.currentTimeMillis());

        byte[] btDataFile = new sun.misc.BASE64Decoder().decodeBuffer(imgSrc);

        File of = new File(IMG_DIRECTORY + fileName + IMG_EXTENSION);

        FileOutputStream osf = new FileOutputStream(of);
        osf.write(btDataFile);
        osf.flush();
        return of;
    }

    private void wait(WebDriver driver, long duration, TimeUnit timeunit) {
        driver.manage().timeouts().implicitlyWait(duration, timeunit);
    }

}
