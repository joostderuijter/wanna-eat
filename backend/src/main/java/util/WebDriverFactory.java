package util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverFactory {

    public static WebDriver createChromeDriver() {
        setChromeDriverPath();
        return new ChromeDriver();
    }

    public static WebDriver createHeadlessChromeDriver() {
        setChromeDriverPath();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        return new ChromeDriver(options);
    }

    private static void setChromeDriverPath() {
        System.setProperty("webdriver.chrome.driver", "backend/src/main/resources/chromedriver");
    }
}
