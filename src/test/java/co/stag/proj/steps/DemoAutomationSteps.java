package co.stag.proj.steps;

import co.stag.config.SeleniumDriver;
import co.stag.lib.SeleniumLib;
import co.stag.proj.pages.Pages;
import co.stag.proj.util.Debugger;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.List;

public class DemoAutomationSteps extends Pages {

    public DemoAutomationSteps(SeleniumDriver driver) {
        super(driver);
    }

    @Given("the user open {string} application")
    public void theUserOpenApplication(String url) {
        boolean testResult = false;
       String vMessage = demoAutomation.openApplication(url);
       if (!vMessage.equalsIgnoreCase("Success")){
           testResult = true;
           Debugger.println("could not open application");
           SeleniumLib.takeAScreenShot("couldNotOpenApplication.jpg");
       }
        Assert.assertFalse(vMessage,testResult);
    }


    @When("the user verify page title {string}")
    public void theUserVerifyPageTitleAutomationDemoSite(String pageTitle) {
        boolean testResult = false;
        String vMessage = demoAutomation.verifyPageTitle(pageTitle);
        if (!vMessage.equalsIgnoreCase("Success")){
            testResult = true;
            Debugger.println("Page Title is not present ");
            SeleniumLib.takeAScreenShot("pageTitleIsNotPresent.jpg");
        }

        Assert.assertFalse(vMessage,testResult);
    }

    @Then("the user verify rigister page fields")
    public void theUserVerifyRigisterPageFields(DataTable exp_values) {
        boolean testResult = false;
        List<String> expFields = exp_values.asList();
        for (String field : expFields) {
            String vMessage = demoAutomation.verifyRegisterPageFields(field);
            if (!vMessage.equalsIgnoreCase("Success")) {
                testResult = true;
                Debugger.println("Register page fields is not present");
                SeleniumLib.takeAScreenShot("registerPageFieldsNotPresent.jpg");
            }
            Assert.assertFalse(vMessage, testResult);
        }
    }

    @And("the user is ranjinth")
    public void theUserIsRanjinth() {

    }
}
