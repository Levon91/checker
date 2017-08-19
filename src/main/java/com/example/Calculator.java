package com.example;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Calculator {

    private static final String IMG_DIRECTORY = "src/main/resources/images/";
    private static final String IMG_EXTENSION = ".png";

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.startShow();
    }

    public void startShow() {
//        FirefoxDriver driver;
        WebDriver driver;
        ITesseract instance;

        try {

//        String torPath = "E:/tor/start.exe";
//        String torProfilePath = "E:/tor/Browser/firefox.exe";

//        File torProfileDir = new File("C:\\Users\\levont\\Desktop\\TorBrowser\\Browser\\TorBrowser\\Data\\Browser\\profile.default");
//        FirefoxBinary binary = new FirefoxBinary(new File("C:\\Users\\levont\\Desktop\\TorBrowser\\Browser\\firefox.exe"));
//        FirefoxProfile torProfile = new FirefoxProfile(torProfileDir);
//        torProfile.setPreference("webdriver.load.strategy", "unstable");
//
//        try {
//            binary.startProfile(torProfile, torProfileDir, "");
//            FirefoxProfile profile = new FirefoxProfile();
//            profile.setPreference("network.proxy.type", 1);
////            profile.setPreference("network.proxy.socks", "127.0.0.1");
//            profile.setPreference("network.proxy.socks_port", 9150);
//            profile.setPreference("network.proxy.socks_version", 5);
//            profile.setPreference("places.history.enabled", false);
//            profile.setPreference("privacy.clearOnShutdown.offlineApps", true);
//            profile.setPreference("privacy.clearOnShutdown.passwords", true);
//            profile.setPreference("privacy.clearOnShutdown.siteSettings", true);
//            profile.setPreference("privacy.sanitize.sanitizeOnShutdown", true);
//            profile.setPreference("signon.rememberSignons", false);
//            profile.setPreference("network.cookie.lifetimePolicy", 2);
//            profile.setPreference("network.dns.disablePrefetch", true);
//            profile.setPreference("network.http.sendRefererHeader", 0);
//            profile.setPreference("permissions.default.image", 2);
//            profile.setPreference("network.proxy.socks_remote_dns", true);
//
//            driver = new FirefoxDriver(profile);
//
//            driver.manage().window().maximize();
//        } catch (IOException e) {
//            System.out.println("ERROR occurred");
//            e.printStackTrace();
//        }

//        FirefoxOptions options = new FirefoxOptions();
//        options.setBinary(binary);

//        File torProfileDir = new File(torProfilePath);
//        File tor = new File(torPath);
//        System.out.println(tor.exists());
//        FirefoxBinary binary = new FirefoxBinary(tor);
//        FirefoxProfile torProfile = new FirefoxProfile(torProfileDir);
//        FirefoxOptions firefoxOptions = new FirefoxOptions();
//        firefoxOptions.setBinary(binary);
//        firefoxOptions.setProfile(torProfile);

            driver = new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
            driver.get("http://report.appa.am/PaapBureau/Report/RequestReport03");

            driver.manage().window().maximize();

            driver.manage().timeouts().pageLoadTimeout(5L, TimeUnit.SECONDS);

            driver.findElement(By.xpath(".//*[@id='vehicleNumber']")).sendKeys("35FT311");
            wait(driver, 3, TimeUnit.SECONDS);

            driver.findElement(By.xpath(".//*[@id='requestDateTime']")).sendKeys("24.08.2017");

            wait(driver, 3, TimeUnit.SECONDS);
            driver.findElement(By.className("ui-datepicker-close")).click();

            WebElement img = driver.findElement(By.xpath("//img[contains(@title,'Փոխել')]"));

            String imgSrc = img.getAttribute("src");

            File of = createImageFile(imgSrc);

            File converted = grayScaleImage(of);

            if (converted.exists()) {
                System.out.println("SCALED");
            }

//            String result = getStringFromImage(of.getAbsolutePath());

            String result;
            instance = new Tesseract();
            result = processImage(instance, of);

            System.out.println(result);

            driver.findElement(By.xpath(".//*[@id='inp-captcha']")).sendKeys("");
            wait(driver, 3, TimeUnit.SECONDS);


            driver.findElement(By.xpath(".//*[@id='isAgree']")).click();
            wait(driver, 3, TimeUnit.SECONDS);

            driver.findElement(By.xpath(".//*[@id='btn-request']")).click();

            wait(driver, 5, TimeUnit.SECONDS);

            driver.quit();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File grayScaleImage(File image) throws IOException {
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

    private void fillPixels(BufferedImage grayScale) {
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

    private void fillPixels2(BufferedImage grayScale) {
        byte[] pixels = ((DataBufferByte) grayScale.getRaster().getDataBuffer()).getData();

        for (int i = 0; i < pixels.length - 1; i++) {
            System.out.println(pixels[i]);
        }
    }

    private BufferedImage changeColor(BufferedImage image, int srcColor, int replaceColor) {
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

//    private String getStringFromImage(final String imageFilePath) {
//        try {
//            final URL tessDataResource = getClass().getResource("/");
//            final File tessFolder = new File(tessDataResource.toURI());
//            final String tessFolderPath = tessFolder.getAbsolutePath();
//            System.out.println(tessFolderPath);
//            BytePointer outText;
//            TessBaseAPI api = new TessBaseAPI();
//            api.SetVariable("tessedit_char_whitelist", "0123456789");
////            Initializeing api
//            if (api.Init(tessFolderPath, "spa") != 0) {
//                System.err.println("ERROR");
//            }
//
//            lept.PIX image = pixRead(imageFilePath);
//            api.SetImage(image);
//            outText = api.GetUTF8Text();
//            String result = outText.getString();
//
//            api.End();
//            outText.deallocate();
//            pixDestroy(image);
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    private String processImage(ITesseract instance, File of) {
        String output = null;
        try {
            output = instance.doOCR(of);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return output;
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
