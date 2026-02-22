package com.insider.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private final By header = By.cssSelector("header.header-insiderone");
    private final By platformMenu = By.xpath("//header//a[contains(text(),'Platform')]");
    private final By footerSection = By.cssSelector("footer.footer");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open(String url) {
        driver.get(url);
        acceptCookies();
        return this;
    }

    public boolean isHeaderDisplayed() {
        return isDisplayed(header);
    }

    public boolean isNavMenuDisplayed() {
        return isDisplayed(platformMenu);
    }

    public boolean isFooterDisplayed() {
        scrollToElement(footerSection);
        return isDisplayed(footerSection);
    }

    public boolean areMainBlocksLoaded() {
        return isHeaderDisplayed()
                && isNavMenuDisplayed()
                && isFooterDisplayed();
    }
}
