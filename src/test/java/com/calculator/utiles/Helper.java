package com.calculator.utiles;

import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {

    public static String takeScreenshot(AppiumDriver driver, String testName) throws IOException {

        String data = new SimpleDateFormat("yyyymmddhmmss").format(new Date(0));
        TakesScreenshot screenshot = (driver);
        File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
        File destination = new File("target/screenshot/"+testName+"_"+data+".png");
        String filePath = destination.getAbsolutePath();
        FileUtils.copyFile(sourceFile, destination);
        return filePath;


    }
}
