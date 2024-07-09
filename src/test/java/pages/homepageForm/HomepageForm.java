package pages.homepageForm;

import org.openqa.selenium.By;
import utils.TestBase;

public class HomepageForm extends TestBase {

    public By bmButton = By.id("react-burger-menu-btn");

    public By logoutButton = By.id("logout_sidebar_link");

    public void clickLogoutButton() {
        driver.findElement(logoutButton).click();
    }

    public void clickBmButton() {
        driver.findElement(bmButton).click();
    }

}
