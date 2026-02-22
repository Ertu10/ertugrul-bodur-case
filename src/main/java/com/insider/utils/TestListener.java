package com.insider.utils;

import com.insider.driver.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Test fail oldugunda otomatik screenshot alir
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("========================================");
        System.out.println("STARTING TEST: " + result.getName());
        System.out.println("========================================");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("TEST PASSED: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("TEST FAILED: " + result.getName());
        System.out.println("Failure reason: " + result.getThrowable().getMessage());

        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            takeScreenshot(driver, result.getName());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("TEST SKIPPED: " + result.getName());
    }

    private void takeScreenshot(WebDriver driver, String testName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String destination = "reports/screenshots/" + fileName;

            File destFile = new File(destination);
            destFile.getParentFile().mkdirs();
            FileUtils.copyFile(source, destFile);

            System.out.println("Screenshot saved: " + destination);
        } catch (IOException e) {
            System.out.println("Screenshot alinamadi: " + e.getMessage());
        }
    }
}
