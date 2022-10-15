package com.calculator.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.calculator.App;
import com.calculator.utiles.Helper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.bys.builder.AppiumByBuilder;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;

public class TestBase {

    protected static AppiumDriver driver;
    protected static ExtentReports extent;
    protected static ExtentSparkReporter spark;
    protected static ExtentTest extentTest;

    @BeforeTest
    public void setUpTestForReport(){
        System.out.println("INFO: Extend Report start tracking.");
        extent = new ExtentReports();
        spark = new ExtentSparkReporter("target/Spark.html");
        extent.attachReporter(spark);
        extent.setSystemInfo("Env", System.getProperty("os.name"));
    }
    @BeforeMethod
    public void setUp() {

        DesiredCapabilities ds = new DesiredCapabilities();
        ds.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        ds.setCapability(MobileCapabilityType.DEVICE_NAME,"sdk_gphone_x86");
        ds.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
        ds.setCapability(MobileCapabilityType.PLATFORM_VERSION,"11.0");
        ds.setCapability("appPackage", "com.bng.calculator");
        ds.setCapability("appActivity", "com.bng.calc.MainActivity");
        System.out.println("INFO: Driver has been initialised.");
        try {
            driver = new AppiumDriver(new URL("http://localhost:4723/wd/hub"), ds);
           driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    // after method would be running after every each in method
    @AfterMethod
    public void tearDown(ITestResult result) throws InterruptedException, IOException {
        System.out.println("INFO: App is closing...");
        if(result.getStatus()==ITestResult.FAILURE){
            String screenshotPath = Helper.takeScreenshot(driver, result.getName());
            extentTest.log(Status.FAIL, result.getThrowable());
            extentTest.addScreenCaptureFromPath(screenshotPath);
            extentTest.fail(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
        Thread.sleep(3000);
        driver.quit();
        extent.flush();
    }

}
