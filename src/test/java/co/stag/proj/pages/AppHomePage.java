package co.stag.proj.pages;

import co.stag.lib.SeleniumLib;
import co.stag.proj.config.AppConfig;
import co.stag.proj.util.Debugger;
import co.stag.proj.util.GlobalVariables;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AppHomePage {

    private SeleniumLib seleniumLib;
    WebDriver driver;
    WebDriverWait wait;

    @FindBy(xpath = "//h2")
    WebElement loginMagPage;

    @FindBy(xpath = "//input[@autocomplete='username']")
    WebElement userName_txtBox;

    @FindBy(xpath = "//button[@type='submit']" )
    WebElement next_btn;

    @FindBy(xpath = "//input[@autocomplete='current-password']")
    WebElement password_txtBox;

    @FindBy(xpath = "//span[@class='dark-primary-color']")
    WebElement applications_txt;

    public AppHomePage(WebDriver driver) {
        this.driver = driver;
        seleniumLib = new SeleniumLib(driver);
        wait = new WebDriverWait(driver,10);
    }

    public String loadApplication() {
        //The URL will be reading from the Config File.
        String appUrl = AppConfig.getPropertyValue("APP_URL");
        if (appUrl == null || appUrl.isEmpty()) {
            return "URL is empty. Please provide URL in Aappconfig.properties file";
        }
        driver.get(appUrl);
        seleniumLib.sleepInSeconds(2);
        return "Success";
    }

    public String verifyAppLoaded(String titleLink) {
        Debugger.println("Verifying page title link...");
        if (!seleniumLib.isElementPresent(loginMagPage)) {
            return "App link is not loaded";
        }
        String actLink = loginMagPage.getText();
        if(!actLink.equalsIgnoreCase(titleLink)){
            return "Expected Link "+titleLink+", actual:"+actLink;
        }
        return "Success";
    }

    public String logInAndVerifyMAGHomePage() {
        String userName= AppConfig.getPropertyValue("ADMIN_USER1");
        String password= AppConfig.getPropertyValue("USER1_PW");
        if (userName == null || userName.isEmpty()) {
            return "userName is empty. Please provide userName in Aappconfig.properties file";
        }
        if (password == null || password.isEmpty()) {
            return "password is empty. Please provide userName in Aappconfig.properties file";
        }
        userName_txtBox.sendKeys(userName);
        next_btn.click();
        seleniumLib.sleepInSeconds(2);
        password_txtBox.sendKeys(password);
        next_btn.click();
        return "Success";

    }

    public String verifyApplicationsInHomePage(String application) {
        try{
        wait.until(ExpectedConditions.visibilityOf(applications_txt));
        String text = applications_txt.getText();
        if(!text.equalsIgnoreCase(application)){
            return "Expected "+application+", actual:"+text;
        }
        }catch (Exception exp){
            return "Failed to Load Home Page"+exp;
        }
        return "Success";
    }
}//end
