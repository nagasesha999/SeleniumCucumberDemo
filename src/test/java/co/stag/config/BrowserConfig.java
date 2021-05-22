package co.stag.config;
//Base Class

import co.stag.proj.util.Debugger;
import org.junit.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BrowserConfig {

    static Properties config;
    public static String browser;

    public static void loadConfig() {
        if (browser != null) {//Already Loaded, No need to Load again.
            return;
        }
        config = loadConfigProperties("BrowserConfig.properties");
        browser = config.getProperty("Browser");
    }

    public static Properties loadConfigProperties(String filename) {
        String configlocation = System.getProperty("user.dir") + File.separator + "config";
        Properties pro = new Properties();
        try {
            pro.load(new FileInputStream(configlocation + File.separator + filename));
        } catch (IOException exp) {
            Debugger.println("File: " + filename + " Not present in location :" + configlocation + ", " + exp);
            Assert.assertFalse("File: " + filename + " Not present in location :" + configlocation, true);
        }
        return pro;
    }

    public static String getBrowser() {
        if (browser == null || browser.isEmpty()) {
            loadConfig();
        }
        return browser;
    }

}//end
