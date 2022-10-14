package com.calculator.tests;

import com.calculator.App;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;

public class TestBase {

    protected static AppiumDriver driver;
    @BeforeMethod
    public void setUp() {
        System.out.println("INFO: Driver has been initialised.");


        DesiredCapabilities ds = new DesiredCapabilities();
        ds.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        ds.setCapability(MobileCapabilityType.DEVICE_NAME,"sdk_gphone_x86");
        ds.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
        ds.setCapability(MobileCapabilityType.PLATFORM_VERSION,"11.0");
        ds.setCapability("appPackage", "com.bng.calculator");
        ds.setCapability("appActivity", "com.bng.calc.MainActivity");


        try {
            driver = new AppiumDriver(new URL("http://localhost:4723/wd/hub"), ds);
           driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));


        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    // after method would be running after every each in method
    @AfterMethod
    public void tearDown(ITestResult result) throws InterruptedException {
        System.out.println("INFO: App is closing...");
        Thread.sleep(3000);
        driver.quit();


    }

}
