package utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class TestBase {

    public static WebDriver driver;
    public static Properties properties;
    String browserName;

    public TestBase() {
        try {
            properties = new Properties();
            FileInputStream file = new FileInputStream("src/main/java/utils/config.properties");
            properties.load(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeBrowserDriver() throws Exception {
        browserName = properties.getProperty("browser");

        if (browserName.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");
            driver = new ChromeDriver();
        } else if (browserName.equalsIgnoreCase("firefox")) {
            System.setProperty("webdriver.firefox.driver", "src/main/resources/drivers/geckodriver.exe");
            driver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("edge")) {
            System.setProperty("webdriver.edge.driver", "src/main/resources/drivers/msedgedriver.exe");
            driver = new EdgeDriver();
        } else {
            throw new Exception("Unsupported browser!!!");
        }
    }

    public void loadTheWebsite() {
        driver.get(properties.getProperty("url"));
        driver.manage().window().maximize();
    }

    public void quitTheDriver() {
        if (browserName == "chrome") {
            driver.quit();
        } else {
            driver.close();
        }
    }
}
