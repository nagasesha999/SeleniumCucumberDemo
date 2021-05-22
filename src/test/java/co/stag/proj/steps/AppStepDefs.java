package co.stag.proj.steps;

import co.stag.config.SeleniumDriver;
import co.stag.proj.pages.Pages;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;

public class AppStepDefs extends Pages {

    public AppStepDefs(SeleniumDriver driver) {
        super(driver);
    }

    //Step1
    @Given("^the user is able to access the application$")
    public void theUserIsAbleToAccessPanelAppApplication() throws Throwable {
        String stepResult = homePage.loadApplication();
        if(!stepResult.equalsIgnoreCase("Success")){
            Assert.fail(stepResult);
        }
    }

    @Then("^the user should see the MAG Login page (.*)$")
    public void theUserShouldSeeTheMAGLoginPage(String linkTitle) {
        String stepResult = homePage.verifyAppLoaded(linkTitle);
        if(!stepResult.equalsIgnoreCase("Success")){
            Assert.fail(stepResult);
        }

    }

    @And("User should login to MAG with valid credential")
    public void userShouldLoginToMAGWithValidCredential() {
        String stepResult = homePage.logInAndVerifyMAGHomePage();
        if(!stepResult.equalsIgnoreCase("Success")){
            Assert.fail(stepResult);
        }
    }


    @Then("User is able to view MAG (.*)$")
    public void userIsAbleToViewMAGApplications(String application) {
        String stepResult = homePage.verifyApplicationsInHomePage(application);
        if(!stepResult.equalsIgnoreCase("Success")){
            Assert.fail(stepResult);
        }
    }


}//end