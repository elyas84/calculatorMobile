package com.calculator.tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.calculator.utiles.Helper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;

public class TestBase {
    protected static AppiumDriver driver;
    protected static ExtentReports extent;
    protected static ExtentSparkReporter spark;
    protected static ExtentTest extentTest;

    protected static AppiumDriverLocalService service;

    public static String NodeExePath = "C:\\Program Files\\nodejs\\node.exe";
    public static String AppiumMainJsPath = "C:\\Users\\elyas\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js";
    public static String ServerAddress = "0.0.0.0";
    @BeforeTest
    public void startAppiumServer() {
        // how to set tp Appium server start programmatically
        service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File(NodeExePath)).withAppiumJS(new File(AppiumMainJsPath)).withIPAddress(ServerAddress)
                .withArgument(GeneralServerFlag.BASEPATH, "/wd/hub").usingPort(4723));
        System.out.println("INFO: starting Appium server...");
        service.start();
    }

    @BeforeMethod
    public void setUp() {

        System.out.println("INFO: Extend Report start tracking.");
        extent = new ExtentReports();
        spark = new ExtentSparkReporter("target/Spark.html");
        extent.attachReporter(spark);
        extent.setSystemInfo("Env", System.getProperty("os.name"));
        DesiredCapabilities ds = new DesiredCapabilities();
        ds.setCapability(MobileCapabilityType.NO_RESET, "true"); // it will clear all caches in the device
        ds.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        ds.setCapability(MobileCapabilityType.DEVICE_NAME, "sdk_gphone_x86");
        ds.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
        ds.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11.0");
        ds.setCapability("appPackage", "com.bng.calculator");
        ds.setCapability("appActivity", "com.bng.calc.MainActivity");
        System.out.println("INFO: Driver has been initialised.");
        try {
            driver = new AppiumDriver(new URL("http://0.0.0.0:4723/wd/hub"), ds);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    // after method would be running after every each in method
    @AfterMethod
    public void tearDown(ITestResult result) throws InterruptedException, IOException {
        System.out.println("INFO: App is closing...");
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = Helper.takeScreenshot(driver, result.getName());
            extentTest.log(Status.FAIL, result.getThrowable());
            extentTest.addScreenCaptureFromPath(screenshotPath);
            extentTest.fail(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
        Thread.sleep(3000);
        driver.quit();
        extent.flush();
    }

    @AfterTest
    public void tearDownAppiumServer(){

        System.out.println("INFO: Appium server now is shooting down");
        if(service.isRunning() == true){
            service.stop();
        }
    }

}
