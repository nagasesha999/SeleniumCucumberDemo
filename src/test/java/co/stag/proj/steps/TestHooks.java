package co.stag.proj.steps;

import co.stag.config.SeleniumDriver;
import co.stag.lib.SeleniumLib;
import co.stag.proj.pages.Pages;
import co.stag.proj.util.Debugger;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class TestHooks extends Pages {

    SeleniumLib seleniumLib;
    public static String currentTagName = "";
    public static String currentTags = "";
    public static String currentFeature = "";
    public static String temptagname = "";
    public static boolean new_scenario_feature = false;

    public TestHooks(SeleniumDriver driver) {
        super(driver);
        seleniumLib = new SeleniumLib(driver);
    }

    @Before
    public void beginingOfTest(Scenario scenario) {
        currentTagName = scenario.getSourceTagNames().toString();
        currentTags = scenario.getSourceTagNames().toString();
        currentFeature = scenario.getId().split(";")[0];
        if (temptagname.isEmpty() || !(temptagname.equalsIgnoreCase(currentTagName))) {
            Debugger.println("\n" + scenario.getSourceTagNames() + " STARTED");
            temptagname = currentTagName;
            new_scenario_feature = true;
            Debugger.println("FEATURE: " + currentFeature.replaceAll("-", " "));
            Debugger.println("SCENARIO: " + scenario.getName());
        } else {
            new_scenario_feature = false;
            Debugger.println("SCENARIO: " + scenario.getName());
        }
    }

    @After(order = 0)
    public void tearDown(Scenario scenario) {
        String scenarioStatus = scenario.getStatus().toString();
        if (!scenarioStatus.equalsIgnoreCase("PASSED")) {
            scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png", "screenshot");
        }
        Debugger.println("STATUS: " + scenarioStatus.toUpperCase());
    }

    @After(order = 1)
    public void afterScenario() {
        //login_page.logoutFromMI();
    }
}//end class
