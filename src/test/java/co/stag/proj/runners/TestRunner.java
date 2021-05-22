package co.stag.proj.runners;


import courgette.api.CourgetteOptions;
import courgette.api.CourgetteRunLevel;
import courgette.api.CucumberOptions;
import courgette.api.junit.Courgette;
import org.junit.runner.RunWith;

@RunWith(Courgette.class)
@CourgetteOptions(
        threads = 6,
        runLevel = CourgetteRunLevel.FEATURE,
        rerunFailedScenarios = false,
        showTestOutput = true,
        reportTitle = "App Report Title",
        reportTargetDir = "target",
        cucumberOptions = @CucumberOptions(
                features = "src/test/resources/features",
                glue = "co.stag.proj.steps",
                tags = {"@DAP_001, not @Deprecated, not @FixRequired, not @Defect, not @Exclude"},
                publish = true,
                plugin = {
                        "pretty",
                        "json:target/cucumber-report/cucumber.json",
                        "html:target/cucumber-report/cucumber.html",
                        "junit:target/cucumber-report/cucumber.xml"
                }
        ))
public class TestRunner {
}