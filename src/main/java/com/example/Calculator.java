package com.example;

import net.sourceforge.tess4j.ITesseract;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Calculator {

    private static final String IMG_DIRECTORY = "./src/main/resources/images/";
    private static final String IMG_EXTENSION = ".png";

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.startShow();
    }

    public void startShow() {
        FirefoxDriver driver;
        ITesseract instance;

//        try {

//        String torPath = "E:/tor/start.exe";
//        String torProfilePath = "E:/tor/Browser/firefox.exe";

        File torProfileDir = new File("C:\\Users\\levont\\Desktop\\TorBrowser\\Browser\\TorBrowser\\Data\\Browser\\profile.default");
        FirefoxBinary binary = new FirefoxBinary(new File("C:\\Users\\levont\\Desktop\\TorBrowser\\Browser\\firefox.exe"));
        FirefoxProfile torProfile = new FirefoxProfile(torProfileDir);
        torProfile.setPreference("webdriver.load.strategy", "unstable");

        try {
            binary.startProfile(torProfile, torProfileDir, "");
            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("network.proxy.type", 1);
//            profile.setPreference("network.proxy.socks", "127.0.0.1");
            profile.setPreference("network.proxy.socks_port", 9150);
            profile.setPreference("network.proxy.socks_version", 5);
            profile.setPreference("places.history.enabled", false);
            profile.setPreference("privacy.clearOnShutdown.offlineApps", true);
            profile.setPreference("privacy.clearOnShutdown.passwords", true);
            profile.setPreference("privacy.clearOnShutdown.siteSettings", true);
            profile.setPreference("privacy.sanitize.sanitizeOnShutdown", true);
            profile.setPreference("signon.rememberSignons", false);
            profile.setPreference("network.cookie.lifetimePolicy", 2);
            profile.setPreference("network.dns.disablePrefetch", true);
            profile.setPreference("network.http.sendRefererHeader", 0);
            profile.setPreference("permissions.default.image", 2);
            profile.setPreference("network.proxy.socks_remote_dns", true);

            driver = new FirefoxDriver(profile);

            driver.manage().window().maximize();
        } catch (IOException e) {
            System.out.println("ERROR occurred");
            e.printStackTrace();
        }

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


//
//            driver.findElement(By.xpath(".//*[@id='vehicleNumber']")).sendKeys("35FT311");
//            wait(driver, 3, TimeUnit.SECONDS);
//
//            driver.findElement(By.xpath(".//*[@id='requestDateTime']")).sendKeys("24.08.2017");
//
//            wait(driver, 3, TimeUnit.SECONDS);
//            driver.findElement(By.className("ui-datepicker-close")).click();
//
//            WebElement img = driver.findElement(By.xpath("//img[contains(@title,'Փոխել')]"));
//
//            String imgSrc = img.getAttribute("src");
//
//            File of = createImageFile(imgSrc);
//
//            boolean isScaled = grayScaleImage(of);
//            if (isScaled) {
//                System.out.println("SCALED");
//            }
//
////            String result = getStringFromImage(of.getAbsolutePath());
//
////            instance = new Tesseract();
////            result = processImage(instance, of, result);
//
////            System.out.println(result);
//
//            driver.findElement(By.xpath(".//*[@id='inp-captcha']")).sendKeys("");
//            wait(driver, 3, TimeUnit.SECONDS);
//
//
//            driver.findElement(By.xpath(".//*[@id='isAgree']")).click();
//            wait(driver, 3, TimeUnit.SECONDS);
//
//            driver.findElement(By.xpath(".//*[@id='btn-request']")).click();
//
//            wait(driver, 5, TimeUnit.SECONDS);
//
//            driver.quit();

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private boolean grayScaleImage(final File image) throws IOException {
        BufferedImage master;
        BufferedImage grayScale;
        BufferedImage blackWhite;
        master = ImageIO.read(new File(image.getAbsolutePath()));
        grayScale = ImageIO.read(new File(image.getAbsolutePath()));
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(grayScale, grayScale);

        blackWhite = new BufferedImage(master.getWidth(), master.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = blackWhite.createGraphics();
        g2d.drawImage(master, 0, 0, null);
        g2d.dispose();
        return ImageIO.write(grayScale, "png", image);
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

//    private static String processImage(ITesseract instance, File of, String result) {
//        try {
//            result = instance.doOCR(of);
//        } catch (TesseractException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    private static File createImageFile(String imgSrc) throws IOException {
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

    private static void wait(WebDriver driver, long duration, TimeUnit timeunit) {
        driver.manage().timeouts().implicitlyWait(duration, timeunit);
    }

}
