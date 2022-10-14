package com.calculator.tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AddTwoNumbers extends TestBase{


    @Test
    void addingTwoNumbers(){

        By digit_01 = By.id("btn_1");
        By digit_02 = By.id("btn_2");
        By addOperation = By.id("plus");
        By equalOperation = By.id("equal");
        By formula = By.id("formula");
        driver.findElement(digit_01).click();
        driver.findElement(addOperation).click();
        driver.findElement(digit_02).click();
        driver.findElement(equalOperation).click();
        String result = driver.findElement(formula).getText();
        Assert.assertEquals(result, "4");
        Assert.assertTrue(driver.findElement(By.id("result")).isDisplayed());

    }
}
