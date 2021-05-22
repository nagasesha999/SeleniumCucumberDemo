package co.stag.proj.pages;

import co.stag.lib.SeleniumLib;
import co.stag.proj.util.Debugger;
import co.stag.proj.util.Wait;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class DemoAutomation {

    private SeleniumLib seleniumLib;
    WebDriver driver;
    WebDriverWait wait;

    @FindBy(xpath = "//h1")
    public WebElement curentPageTitle;

    @FindBy(xpath = "//label[contains(@class,'control-label')]")
    public List<WebElement> registerPageFieldsList;

    public DemoAutomation(WebDriver driver) {
        this.driver = driver;
        seleniumLib = new SeleniumLib(driver);
        wait = new WebDriverWait(driver, 10);
    }

    public String openApplication(String appUrl) {
        driver.get(appUrl);
        seleniumLib.sleepInSeconds(2);
        return "Success";
    }

    public String verifyPageTitle(String pageTitle) {
        try {
            if (!seleniumLib.isElementPresent(curentPageTitle) && !curentPageTitle.getText().equalsIgnoreCase(pageTitle)) {
                Debugger.println("Page title " + pageTitle + " is not present");
                SeleniumLib.takeAScreenShot("PageTitleIsNotPresent.jpg");
                return "Page title " + pageTitle + " is not present";
            }
            return "Success";
        } catch (Exception exp) {
            Debugger.println("Page Title is not present : " + exp);
            SeleniumLib.takeAScreenShot("PageTitleIsNotPresent.jpg");
            return "Page Title is not present : " + exp;
        }
    }

    public String verifyRegisterPageFields(String expField) {
        try {
            SeleniumLib.sleepInSeconds(3);
            if (registerPageFieldsList.size() == 0 || registerPageFieldsList.isEmpty()) {
                Debugger.println("Register page fields is not present");
                SeleniumLib.takeAScreenShot("registerPageFieldNotPresent.jpg");
                return "Register page fields is not present";
            }

            for (int i = 0; i < registerPageFieldsList.size(); i++) {
                if (registerPageFieldsList.get(i).getText().contains(expField)) {
                    Debugger.println("Register page ACTUAL  : " +  registerPageFieldsList.get(i).getText() + " = EXPECTED : " +expField);
                    break;
                }

            }

            return "Success";
        } catch (Exception exp) {
            Debugger.println("Register page fields is not present : " + exp);
            SeleniumLib.takeAScreenShot("registerPageFieldNotPresent.jpg");
            return "Register page fields is not present : " + exp;
        }
    }
}
