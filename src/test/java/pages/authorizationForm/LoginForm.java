package pages.authorizationForm;

import org.openqa.selenium.By;
import utils.TestBase;

public class LoginForm extends TestBase {

    By usernameFieldLocator = By.name("user-name");
    By passwordFieldLocator = By.name("password");
    public By loginButtonLocator = By.cssSelector("input[type='submit']");
    public By errorMessageTextLocator = By.xpath("//h3[@data-test='error']");
    public By title = By.className("title");

    public void fillLoginFields(String username, String password) {
        driver.findElement(usernameFieldLocator).sendKeys(username);
        driver.findElement(passwordFieldLocator).sendKeys(password);
    }

    public void clickLoginButton() {
        driver.findElement(loginButtonLocator).click();
    }

    public void loginWithEmptyPasswordField(String username, String password) {
        driver.findElement(usernameFieldLocator).sendKeys(username, password);
    }

    public void loginWithEmptyUsernameField(String username, String password){
        driver.findElement(passwordFieldLocator).sendKeys(username, password);
    }
}
