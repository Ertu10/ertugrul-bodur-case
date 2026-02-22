package com.insider.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CareersPage extends BasePage {

    private final By teamsSection = By
            .cssSelector("#career-find-our-calling, .find-our-calling, .career-find-our-calling");
    private final By locationsSection = By.cssSelector("#career-our-location, .our-location, .career-our-location");
    private final By lifeAtInsiderSection = By.cssSelector(".life-at-insider, #life-at-insider");

    public CareersPage(WebDriver driver) {
        super(driver);
    }

    public CareersPage open(String url) {
        driver.get(url);
        acceptCookies();
        return this;
    }

    public boolean isTeamsSectionDisplayed() {
        scrollToElement(teamsSection);
        return isDisplayed(teamsSection);
    }

    public boolean isLocationsSectionDisplayed() {
        scrollToElement(locationsSection);
        return isDisplayed(locationsSection);
    }

    public boolean isLifeAtInsiderSectionDisplayed() {
        scrollToElement(lifeAtInsiderSection);
        return isDisplayed(lifeAtInsiderSection);
    }

    public boolean isCareersPageLoaded() {
        return isTeamsSectionDisplayed()
                && isLocationsSectionDisplayed()
                && isLifeAtInsiderSectionDisplayed();
    }
}
