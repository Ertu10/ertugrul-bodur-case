package com.insider.ui;

import com.insider.config.ConfigReader;
import com.insider.driver.DriverFactory;
import com.insider.pages.*;
import com.insider.utils.TestListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

@Listeners(TestListener.class)
public class InsiderCareerTest {

        private WebDriver driver;
        private HomePage homePage;
        private QualityAssurancePage qaPage;
        private OpenPositionsPage openPositionsPage;
        private LeverApplicationPage leverPage;

        @BeforeClass
        @Parameters({ "browser" })
        public void setUp(@Optional("chrome") String browser) {
                driver = DriverFactory.createDriver(browser);
        }

        @AfterClass
        public void tearDown() {
                DriverFactory.quitDriver();
        }

        @Test(priority = 1, description = "Verify Insider home page is opened and all main blocks are loaded")
        public void testHomePageIsOpenedAndMainBlocksLoaded() {
                homePage = new HomePage(driver);
                homePage.open(ConfigReader.getBaseUrl());

                Assert.assertTrue(homePage.isHeaderDisplayed(),
                                "Header/Navbar should be displayed on home page");
                Assert.assertTrue(homePage.isNavMenuDisplayed(),
                                "Navigation menu should be displayed on home page");
                Assert.assertTrue(homePage.isFooterDisplayed(),
                                "Footer should be displayed on home page");

                System.out.println("Step 1: Ana sayfa basariyla acildi, tum ana bloklar yuklendi");
        }

        @Test(priority = 2, dependsOnMethods = "testHomePageIsOpenedAndMainBlocksLoaded", description = "Navigate to QA Careers, filter jobs by Istanbul/Turkiye and QA department")
        public void testFilterQAJobsByLocationAndDepartment() {
                qaPage = new QualityAssurancePage(driver);
                qaPage.open(ConfigReader.getQACareersUrl());

                Assert.assertTrue(qaPage.isPageLoaded(),
                                "QA Careers page should be loaded");

                openPositionsPage = qaPage.clickSeeAllQAJobs();

                openPositionsPage.filterByLocation("Istanbul, Turkiye");
                openPositionsPage.filterByDepartment("Quality Assurance");

                Assert.assertTrue(openPositionsPage.isJobsListPresent(),
                                "Jobs list should be present after filtering by Istanbul, Turkiye and Quality Assurance");

                int jobsCount = openPositionsPage.getJobsCount();
                System.out.println("Step 2: Istanbul, Turkiye'de " + jobsCount + " QA ilani bulundu");
        }

        @Test(priority = 3, dependsOnMethods = "testFilterQAJobsByLocationAndDepartment", description = "Verify all job details contain correct Position, Department, and Location")
        public void testAllJobsHaveCorrectDetails() {
                List<WebElement> jobs = openPositionsPage.getJobsList();

                Assert.assertFalse(jobs.isEmpty(),
                                "Jobs list should not be empty");

                boolean allJobsValid = openPositionsPage.validateAllJobDetails(
                                new String[] { "Quality Assurance", "Quality", "QA" },
                                "Quality Assurance",
                                "Istanbul");

                Assert.assertTrue(allJobsValid,
                                "All jobs should have QA-related Position, " +
                                                "Department containing 'Quality Assurance', " +
                                                "and Location containing 'Istanbul, Turkiye'");

                System.out.println("Step 3: Tum " + jobs.size()
                                + " ilan icin Position, Department ve Location dogrulandi");
        }

        @Test(priority = 4, dependsOnMethods = "testAllJobsHaveCorrectDetails", description = "Click View Role and verify redirect to Lever Application form")
        public void testViewRoleRedirectsToLeverApplicationPage() {
                leverPage = openPositionsPage.clickViewRole();

                Assert.assertTrue(leverPage.isLeverPageOpened(),
                                "Clicking 'View Role' should redirect to a Lever Application form page (lever.co). " +
                                                "Current URL: " + leverPage.getCurrentUrl());

                System.out.println("Step 4: Lever basvuru sayfasina yonlendirildi: " + leverPage.getCurrentUrl());
        }
}
