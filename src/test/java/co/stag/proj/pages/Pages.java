package co.stag.proj.pages;

import co.stag.config.SeleniumDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class Pages {

    protected WebDriver driver;

    //We have to initialize all the Pages Created in this class.
    protected AppHomePage homePage;
    protected DemoAutomation demoAutomation;

    public Pages(SeleniumDriver driver) {
        this.driver = driver;
        PageObjects();
    }

    public void PageObjects() {

        homePage = PageFactory.initElements(driver, AppHomePage.class);
        demoAutomation = PageFactory.initElements(driver,DemoAutomation.class);
    }
}//end class
