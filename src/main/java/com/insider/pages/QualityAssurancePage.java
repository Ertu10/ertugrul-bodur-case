package com.insider.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class QualityAssurancePage extends BasePage {

    private final By seeAllQAJobsButton = By.cssSelector("a[href*='open-positions'][href*='qualityassurance']");
    private final By seeAllQAJobsButtonAlt = By.xpath("//a[contains(text(),'See all QA jobs')]");
    private final By pageTitle = By.cssSelector("h1");

    public QualityAssurancePage(WebDriver driver) {
        super(driver);
    }

    public QualityAssurancePage open(String url) {
        driver.get(url);
        acceptCookies();
        return this;
    }

    public OpenPositionsPage clickSeeAllQAJobs() {
        scrollToElement(seeAllQAJobsButton);
        try {
            click(seeAllQAJobsButton);
        } catch (Exception e) {
            clickWithJS(seeAllQAJobsButtonAlt);
        }
        // Open positions sayfasina gectigini dogrula
        wait.until(ExpectedConditions.urlContains("open-positions"));
        return new OpenPositionsPage(driver);
    }

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle);
    }
}
