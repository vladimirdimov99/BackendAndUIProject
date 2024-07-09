package uiTests.LoginTests;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.authorizationForm.LoginForm;
import pages.homepageForm.HomepageForm;
import utils.TestBase;

import java.time.Duration;

public class LoginTests extends TestBase {

    Duration timeout = Duration.ofSeconds(3);
    LoginForm loginForm = new LoginForm();
    HomepageForm homepageForm = new HomepageForm();

    @BeforeMethod
    void setUp() throws Exception {
        initializeBrowserDriver();
        loadTheWebsite();
    }

    @Test
    void loginToTheWebsite() {
        loginForm.fillLoginFields(properties.getProperty("username"), properties.getProperty("password"));
        loginForm.clickLoginButton();
        new WebDriverWait(driver, timeout.getSeconds()).until(ExpectedConditions.presenceOfElementLocated(loginForm.title));
        String titleText = driver.findElement(loginForm.title).getText();
        Assert.assertEquals(titleText, "Products");
    }

    @Test
    void loginWithEmptyPasswordField() {
        loginForm.loginWithEmptyPasswordField(properties.getProperty("username"), "");
        loginForm.clickLoginButton();
        new WebDriverWait(driver, timeout.getSeconds()).until(ExpectedConditions.presenceOfElementLocated(loginForm.errorMessageTextLocator));
        Assert.assertEquals(driver.findElement(loginForm.errorMessageTextLocator).getText(), "Epic sadface: Password is required");
    }

    @Test
    void loginWithEmptyUsernameField() {
        loginForm.loginWithEmptyUsernameField("", properties.getProperty("password"));
        loginForm.clickLoginButton();
        new WebDriverWait(driver, timeout.getSeconds()).until(ExpectedConditions.presenceOfElementLocated(loginForm.errorMessageTextLocator));
        Assert.assertEquals(driver.findElement(loginForm.errorMessageTextLocator).getText(), "Epic sadface: Username is required");
    }

    @Test(dependsOnMethods = {"loginToTheWebsite"})
    void logoutFromWebsite() {
        loginForm.fillLoginFields(properties.getProperty("username"), properties.getProperty("password"));
        loginForm.clickLoginButton();
        new WebDriverWait(driver, timeout.getSeconds()).until(ExpectedConditions.elementToBeClickable(homepageForm.bmButton));
        homepageForm.clickBmButton();
        new WebDriverWait(driver, timeout.getSeconds()).until(ExpectedConditions.elementToBeClickable(homepageForm.logoutButton));
        homepageForm.clickLogoutButton();
    }

    @AfterMethod
    void closeTheDriver() {
        quitTheDriver();
    }
}
