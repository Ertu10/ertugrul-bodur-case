package com.insider.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

public class OpenPositionsPage extends BasePage {

    private final By locationSelect = By.cssSelector("select[name='filter-by-location']");
    private final By departmentSelect = By.cssSelector("select[name='filter-by-department']");

    private final By jobsList = By.cssSelector("div.position-list-item");
    private final By jobPositionTitle = By.cssSelector("p.position-title");
    private final By jobDepartment = By.cssSelector("span.position-department");
    private final By jobLocation = By.cssSelector("div.position-location");

    public OpenPositionsPage(WebDriver driver) {
        super(driver);
    }

    public OpenPositionsPage filterByLocation(String location) {
        WebElement selectElement = waitForClickable(locationSelect);
        Select select = new Select(selectElement);
        select.selectByVisibleText(location);
        waitForJobsToReload();
        return this;
    }

    public OpenPositionsPage filterByDepartment(String department) {
        WebElement selectElement = waitForClickable(departmentSelect);
        Select select = new Select(selectElement);
        select.selectByVisibleText(department);
        waitForJobsToReload();
        return this;
    }

    // Filtre degisikliginden sonra AJAX ile ilan listesinin yeniden yuklenmesini
    // bekler
    private void waitForJobsToReload() {
        // AJAX request'in baslamasi icin kisa bekleme
        sleep(1000);

        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(StaleElementReferenceException.class);

        fluentWait.until(d -> {
            List<WebElement> items = d.findElements(jobsList);
            return !items.isEmpty();
        });
    }

    public List<WebElement> getJobsList() {
        try {
            return waitForPresenceOfAll(jobsList);
        } catch (Exception e) {
            return driver.findElements(jobsList);
        }
    }

    public boolean isJobsListPresent() {
        return !getJobsList().isEmpty();
    }

    public int getJobsCount() {
        return getJobsList().size();
    }

    /**
     * Tum ilanlarin Position, Department ve Location bilgilerini dogrular.
     * Position kontrolu OR mantigi ile calisir (QA ilanlari farkli basliklara sahip
     * olabilir).
     */
    public boolean validateAllJobDetails(String[] positionKeywords, String expectedDepartment,
            String expectedLocation) {
        List<WebElement> jobs = getJobsList();
        if (jobs.isEmpty())
            return false;

        for (WebElement job : jobs) {
            String position = job.findElement(jobPositionTitle).getText().toLowerCase();
            String department = job.findElement(jobDepartment).getText();
            String location = job.findElement(jobLocation).getText();

            boolean positionMatches = false;
            for (String keyword : positionKeywords) {
                if (position.contains(keyword.toLowerCase())) {
                    positionMatches = true;
                    break;
                }
            }
            if (!positionMatches) {
                System.out.println("Position uyumsuz: " + job.findElement(jobPositionTitle).getText());
                return false;
            }
            if (!department.toLowerCase().contains(expectedDepartment.toLowerCase())) {
                System.out.println(
                        "Department uyumsuz: beklenen='" + expectedDepartment + "', bulunan='" + department + "'");
                return false;
            }
            if (!location.toLowerCase().contains(expectedLocation.toLowerCase())) {
                System.out.println("Location uyumsuz: beklenen='" + expectedLocation + "', bulunan='" + location + "'");
                return false;
            }
        }
        return true;
    }

    public LeverApplicationPage clickViewRole() {
        List<WebElement> jobs = getJobsList();
        if (jobs.isEmpty()) {
            throw new RuntimeException("Listelenmiş iş ilanı bulunamadı");
        }

        WebElement firstJob = jobs.get(0);
        scrollToElement(firstJob);
        hoverOver(firstJob);

        WebElement viewRole = firstJob.findElement(By.xpath(".//a[contains(text(),'View Role')]"));
        wait.until(ExpectedConditions.elementToBeClickable(viewRole));
        clickWithJS(viewRole);

        waitForNumberOfWindows(2);
        switchToNewWindow();

        return new LeverApplicationPage(driver);
    }
}
