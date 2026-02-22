package com.insider.pages;

import org.openqa.selenium.WebDriver;

public class LeverApplicationPage extends BasePage {

    public LeverApplicationPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLeverPageOpened() {
        try {
            waitForUrlContains("lever.co");
            return true;
        } catch (Exception e) {
            System.out.println("View Role sonrasi URL: " + driver.getCurrentUrl());
            return false;
        }
    }
}
