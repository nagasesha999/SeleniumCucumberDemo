package co.stag.proj.util;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Wait {

    protected static WebDriverWait wait;
    protected static WebDriver webDriver;

    public Wait(WebDriver driver) {
        webDriver = driver;
    }

    public static void forElementToBeDisplayed(WebDriver driver, WebElement element) {
        wait = new WebDriverWait(driver, 100);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /*
	Added this method to verify the element is actually displayed after the specified waiting period.
	 */
    public static boolean isElementDisplayed(WebDriver driver, WebElement element, int seconds) {
        try {
            wait = new WebDriverWait(driver, seconds);
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (Exception exp) {
            return false;
        }
    }

    public static void forElementToBeDisplayed(WebDriver driver, WebElement element, int timeInSeconds) {
        wait = new WebDriverWait(driver, timeInSeconds);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static void forElementToBeClickable(WebDriver driver, WebElement element) {
        try {
            wait = new WebDriverWait(driver, 50);
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception exp) {
            Debugger.println("Exception from waiting for element to be clickable...." + element + "..Waiting for 30 more seconds...");
            wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.elementToBeClickable(element));
        }
    }

    public static void forElementToDisappear(WebDriver driver, By locator) {
        wait = new WebDriverWait(driver, 40);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void forNumberOfElementsToBeGreaterThan(WebDriver driver, By locator, int number) {
        wait = new WebDriverWait(driver, 100);
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, number));
    }

    public static void forNumberOfElementsToBeEqualTo(WebDriver driver, By locator, int number) {
        try {
            Debugger.println("Closing Cookies Banner.");
            wait = new WebDriverWait(driver, 100);
            wait.until(ExpectedConditions.numberOfElementsToBe(locator, number));
        } catch (Exception exp) {
            Debugger.println("Exception from closing cookies banner: " + exp);
        }
    }

    public static void forURLToContainSpecificText(WebDriver driver, String text) {
        wait = new WebDriverWait(driver, 100);
        wait.until(ExpectedConditions.urlContains(text));
    }

    public static void forAlertToBePresent(WebDriver driver) {
        wait = new WebDriverWait(driver, 100);
        wait.until(ExpectedConditions.alertIsPresent());
    }

    public static void seconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void forPageToBeLoaded(WebDriver driver) {
        new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }
}
