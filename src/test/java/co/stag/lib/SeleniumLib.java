package co.stag.lib;

import co.stag.proj.util.Debugger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Point;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;

public class SeleniumLib {

    private static WebDriver driver;
    private static boolean HIGHLIGHT = true;
    private static WebElement webElement = null;
    private static List<WebElement> webElementList = null;

    private String strtext;
    public static String ParentWindowID = null;

    public SeleniumLib(WebDriver driver) {
        SeleniumLib.driver = driver;
    }

    private static Logger LOGGER = LoggerFactory.getLogger(SeleniumLib.class);
    static String defaultSnapshotLocation = System.getProperty("user.dir") + File.separator + "snapshots" + File.separator;

    private static String timeoutErrorMessage(By element) {
        return "Unable to find visibility of element located by " + element + " within " + "10" + " seconds \n "
                + "\nThere several reasons why this may have occurred including: \n" +
                "\n - The page did not finish loading some ui features before the expected timeout (usually related to Ajaxian elements)" +
                "\n - The ui element is legitimately missing from the page due to a bug or feature change " +
                "\n - There was a problem with communication within the Selenium stack" +
                "\nSee image below (if available) for clues.\n If all else fails, RERUN THE TEST MANUALLY! - CPK\n\n";
    }


    public static boolean isClickable(By element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            final WebElement el = driver.findElement(element);
            wait.until(ExpectedConditions.elementToBeClickable(el));
            return true;
        } catch (Exception e) {
            Debugger.println("Element is not clickable: " + e);
            return false;
        }
    }

    public static WebElement waitForElementVisible(By element) {
        WebDriverWait wait = new WebDriverWait(driver, 40);
        final WebElement el = driver.findElement(element);

        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {

                return el.isDisplayed();
            }
        });
        return el;
    }

    /**
     * @param element
     * @return
     */
    public WebElement getElement(By element) {
        try {
            webElement = waitForElementVisible(element);
            return webElement;
        } catch (NoSuchElementException e) {
            Debugger.println("[Error]" + element.toString() + " Not Found ");
            throw e;
        }
    }

    /**
     * @param element
     */
    public static void elementHighlight(WebElement element) {
        JavascriptExecutor javascript = (JavascriptExecutor) driver;
        javascript.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
                "color: pink; border: 3px solid red;");
        javascript.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");

    }

    public void scrollToElement(WebElement element) {
        try {
            if (element == null) {
                return;
            }
            Point location = element.getLocation();
            String script = "scroll(" + location.x + "," + (location.y - 120) + ")";
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript(script);
        } catch (Exception e) {

        }
    }

    /**
     * @param element
     */
    public void clickOnElement(By element) {
        WebElement webele = null;
        try {
            webele = getElement(element);
            webele.click();
        } catch (Exception exp) {
            Debugger.println("SeleniumLib: Click exception on ...." + element.toString() + exp);
            try {
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript("arguments[0].click();", webele);
            } catch (Exception exp1) {
                Debugger.println("SeleniumLib: Click exception1 on ...." + element.toString() + exp);
                Actions actions = new Actions(driver);
                actions.moveToElement(driver.findElement(element)).click().build().perform();
                throw exp1;
            }
        }
    }

    public void clickOnWebElement(WebElement webele) {
        //Debugger.println("Clicking on Web Element..."+webele.toString());
        try {
            webele.click();
        } catch (Exception exp) {
            Debugger.println("WebElement Click exception1 on ...." + webele.toString());
            try {
                Actions actions = new Actions(driver);
                actions.moveToElement(webele).click();
            } catch (Exception exp1) {
                Debugger.println("Webelement Click exception2 on ...." + webele.toString());
                throw exp1;
            }
        }
    }

    public List<WebElement> getElements(By ele) {
        try {

            waitForElementVisible(driver.findElement(ele));
            return driver.findElements(ele);
        } catch (NoSuchElementException exp) {
            //Debugger.println("E5.[Error]" + ele.toString() + " Not Found ");
            return null;
        }
    }

    public void clearValue(By element) {
        try {
            webElement = getWebElement(element);
            webElement.clear();
        } catch (NoSuchElementException e) {
            LOGGER.error("[Error]" + element.toString() + " Not found");
        }
    }

    public void sendValue(By element, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        try {
            webElement = getWebElement(element);
            webElement.clear();
            webElement.sendKeys(value);
        } catch (NoSuchElementException J) {
            LOGGER.error("element not found " + element);
            throw new NoSuchElementException(timeoutErrorMessage(element) + J);
        } catch (Exception exp) {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            webElement = wait.until(ExpectedConditions.elementToBeClickable(webElement));
            webElement.sendKeys(value);
        }
    }

    public void sendValue(WebElement element, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        try {
            element.clear();
            element.sendKeys(value);
        } catch (NoSuchElementException J) {
            Debugger.println("element not found " + element);
        } catch (Exception exp) {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            element = wait.until(ExpectedConditions.elementToBeClickable(element));
            element.sendKeys(value);
        }
    }

    public void focusElement(By element) {
        webElement = getElement(element);
        JavascriptExecutor javascript = (JavascriptExecutor) driver;
        javascript.executeScript("arguments[0].focus();", webElement);
    }

    public void sendKey(By element, Keys key) {
        try {
            webElement = getWebElement(element);
            webElement.sendKeys(key);
        } catch (NoSuchElementException J) {
            LOGGER.error("element not found " + element);
            throw new NoSuchElementException(timeoutErrorMessage(element) + J);
        }
    }

    public String selectFromListByText(By element, String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        try {
            webElement = getElement(element);
            if (webElement == null) {
                Debugger.println("element is null " + webElement);
                return "Web element not present: " + element;
            }
            elementHighlight(webElement);
            // new Select(getElement(element)).selectByVisibleText(text);
            new Select(webElement).selectByVisibleText(text);
            return "Success";
        } catch (NoSuchElementException e) {
            try {
                Select select = new Select(webElement);
                if (select == null) {
                    return "Web element is null: " + element;
                }
                List<WebElement> options = select.getOptions();
                for (WebElement option : options) {
                    String originalText = text.trim().replace(" ", "").replace(" ", "").toLowerCase();
                    String expectedString = option.getText().trim().replace(" ", "").toLowerCase();
                    //Debugger.println("Original....."+originalText+",Exp.."+expectedString);
                    if (originalText.equalsIgnoreCase(expectedString)) {
                        select.selectByVisibleText(option.getText());
                        //Debugger.println("Yes..got it..........");
                        return "Success";
                    }
                }
            } catch (Exception exp) {
                return "Error from Finding element: " + element;
            }
            return "No found";
        }
    }

    public String optionFromListByText(By element, String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        try {
            webElementList = driver.findElements(element);
            if (webElementList == null) {
                Debugger.println("element list is null " + webElementList);
                return "Web element list is empty: " + element;
            }
            for (WebElement actWebelement : webElementList) {
                String actualText = text.trim().replace(" ", "").replace(" ", "").toLowerCase();
                String expectedText = actWebelement.getText().trim().replace(" ", "").toLowerCase();
                if (actualText.equalsIgnoreCase(expectedText)) {
                    actWebelement.click();
                    return "Success";
                }
            }
            return "Option : " + text + " Not Present";
        } catch (NoSuchElementException e) {
            return "Exception Occured while selecting from DropDown List " + e;
        }
    }

    public static void selectFromListByValue(By element, String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        try {
            webElement = getWebElement(element);
            Select select = new Select(webElement);
            select.selectByValue(text);
        } catch (NoSuchElementException e) {
            Debugger.println("element not found  " + element);
        }
    }

    public static boolean IsDisplayed(By element) {
        try {
            webElement = getWebElement(element);
            if (webElement == null) {
                return false;
            }
            elementHighlight(webElement);
            return webElement.isDisplayed();
        } catch (Exception exp) {
            Debugger.println("Element not Displayed......" + exp + "\nElement..." + element);
            return false;
        }
    }

    public boolean highLightWebElement(WebElement element) {
        try {
            if (element != null) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
                elementHighlight(element);
                return true;
            }
            return false;
        } catch (NoSuchElementException e) {
            Debugger.println("[Error]" + element.toString() + "  Not displayed");
            return false;
        }
    }

    public boolean highLightElement(By element) {
        try {
            webElement = getWebElement(element);
            if (webElement != null) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", webElement);
                elementHighlight(webElement);
                return true;
            }
            return false;
        } catch (NoSuchElementException e) {
            Debugger.println("[Error]" + element.toString() + "  Not displayed");
            return false;
        }
    }

    public boolean isElementPresent(By element) {
        try {

            webElement = getWebElement(element);
            if (webElement == null) {
                return false;
            }
            elementHighlight(webElement);
            return webElement.isDisplayed();

        } catch (Exception e) {
            Debugger.println("[Error]" + element.toString() + "  Not displayed");
            return false;
        }
    }

    public boolean isElementClickable(By element) {
        try {
            webElement = getWebElement(element);
            if (webElement == null) {
                return false;
            }
            elementHighlight(webElement);
            return webElement.isEnabled();

        } catch (Exception e) {
            Debugger.println("[Error]" + element.toString() + "  Not Enabled");
            return false;
        }
    }

    public boolean isElementPresent(WebElement element) {
        try {
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException e) {

        }
        return false;
    }

    public void waitForElementVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        final WebElement el = element;
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return el.isDisplayed();
            }
        });
    }

    /**
     * @param element
     * @return
     */
    public static WebElement wait(By element) {

        FluentWait<WebDriver> wait = new WebDriverWait(driver, 50);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        } catch (NoSuchElementException J) {
            throw new NoSuchElementException(timeoutErrorMessage(element) + J);
        } catch (TimeoutException e) {
            throw new TimeoutException(timeoutErrorMessage(element) + e);
        }
        return driver.findElement(element);
    }

    /**
     * @param element
     */
    public boolean JavaScriptClick(By element) {
        try {
            webElement = getWebElement(element);
            if (webElement == null) {
                return false;
            }
            elementHighlight(webElement);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", webElement);
            return true;
        } catch (Exception exp) {
            Debugger.println("Exception: SeleniumLib: Javascript Click.." + exp);
            return false;
        }
    }

    public static WebElement getWebElement(By by) {
        try {
            WebElement element = new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(by));
            return element;
        } catch (Exception exp) {
            return null;
        }
    }

    public static void refreshPage() {
        driver.navigate().refresh();
    }

    /**
     * @param element
     * @return
     */
    public String getText(By element) {
        try {
            webElement = waitForElementVisible(element);
            if (webElement == null) {
                return "";
            }
            elementHighlight(webElement);
            strtext = webElement.getText();
            return "" + strtext;

        } catch (Exception ex) {

            webElement = driver.findElement(element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", webElement);
            try {
                elementHighlight(webElement);
                strtext = webElement.getText();
            } catch (Exception exp1) {
                return "";
            }
            return strtext;
        }
    }

    public String getText(WebElement element) {
        try {

            elementHighlight(element);
            strtext = element.getText();
            return "" + strtext;

        } catch (Exception ex) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
            try {
                elementHighlight(element);
                strtext = element.getText();
            } catch (Exception exp1) {
                return "";
            }
            return strtext;
        }
    }

    /**
     * @param i
     */
    public static void sleep(int i) {
        try {
            Thread.sleep(i * 2000);
        } catch (InterruptedException exp) {

        }
    }

    public static void sleepInSeconds(int i) {
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException exp) {

        }
    }

    public void waitForAjax(int timeoutInSeconds) {
        //Checking active ajax calls by calling jquery.active
        try {
            if (driver instanceof JavascriptExecutor) {
                JavascriptExecutor jsDriver = (JavascriptExecutor) driver;

                for (int i = 0; i < timeoutInSeconds; i++) {
                    Object numberOfAjaxConnections = jsDriver.executeScript("return jQuery.active");
                    // return should be a number
                    if (numberOfAjaxConnections instanceof Long) {
                        Long n = (Long) numberOfAjaxConnections;

                        if (n.longValue() == 0L)
                            break;
                    }
                    Thread.sleep(1000);
                }
            } else {
                LOGGER.error("Web driver: " + driver + " cannot execute javascript");
            }
        } catch (InterruptedException e) {
            LOGGER.error("Ajax wait Exception  " + e);
        }
    }

    /**
     *
     */
    public void ChangeWindow() {
        Set<String> windows = driver.getWindowHandles();
        for (String window : windows) {
            driver.switchTo().window(window);
        }
    }

    /**
     *
     */
    public void ChangeToParentWindow() {
        driver.switchTo().window(ParentWindowID);
    }

    public void clickonLink(String string) {
        if (string == null || string.isEmpty()) {
            return;
        }
        waitForAjax(2);
        driver.findElement(By.linkText(string)).click();
        waitForAjax(4);
    }

    public static void clickLink(String text) {
        try {
            if (text == null || text.isEmpty()) {
                return;
            }
            driver.findElement(By.linkText(text)).isDisplayed();
            driver.findElement(By.linkText(text)).click();
        } catch (NoSuchElementException J) {
            Debugger.println("element not found " + text);
            //throw new NoSuchElementException (timeoutErrorMessage() + J);
        }
    }
    //   .................. upload file method.............

    public static boolean upload(String path) {
        WebElement element;
        // Switch to newly opened window
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
        sleep(2);
        element = new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("file")));
        if (element != null) {
            element.click();
        }
        sleep(2);
        //Copy file path to cllipboard
        StringSelection ss = new StringSelection(path);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

        //Java Robot commands to paste the clipboard copy on focused textbox
        Robot robot = null;
        try {
            robot = new Robot();

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            sleep(2);
            element = new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/form/div/p/input[1]")));
            element.click();
            sleep(2);
            element = new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='Close Window']")));
            element.click();
            sleep(2);
            return true;
        } catch (Exception exp) {
            Debugger.println("Upload Exception from SeleniumLib: " + exp);
            return false;
        }
    }

    public static boolean isTextPresent(String text) {
        try {
            if (text == null) {
                return false;
            }
            return getVisibleText().contains(text);
        } catch (Exception exp) {
            LOGGER.error("element not found  " + exp);
            return false;
        }
    }

    /**
     * @return
     */
    public static String getVisibleText() {
        try {
            return driver.findElement(By.tagName("body")).getText();
        } catch (Exception exp) {
            return "";
        }
    }

    //-------------------------	//Added by STAG on 26-11-2016.------------------------------------------------
    public boolean moveMouseAndClickOnElement(By element) {
        try {
            Actions action = new Actions(driver);
            WebElement we = driver.findElement(element);
            action.moveToElement(we).build().perform();
            action.click(we).build().perform();
            return true;
        } catch (Exception exp) {
            return false;
        }
    }

    public void mouseHoverElement(By element) {
        Actions action = new Actions(driver);
        WebElement we = driver.findElement(element);
        action.moveToElement(we).build().perform();
    }

    public String getAttributeValue(By element, String attribute) {
        try {
            webElement = getElement(element);
            if (webElement == null) {
                return null;
            }
            elementHighlight(webElement);
            return webElement.getAttribute(attribute);
        } catch (NoSuchElementException e) {
            Debugger.println("[Error] Selenium Lib....getAttributeValue..." + element.toString() + "Not found");
            return null;
        }
    }

    // Add by manjunath K M 12-9-16
    public List<WebElement> getHeadingElements(By element) {
        try {
            waitForElementVisible(driver.findElement(element));
            return driver.findElements(element);
        } catch (NoSuchElementException exp) {
            Debugger.println("SeleniumLib: [Error]" + element.toString() + " Not Found ");
            return null;
        }
    }

    public int getNoOfRows(By element) {
        try {
            waitForElementVisible(driver.findElement(element));
            return driver.findElements(element).size();
        } catch (NoSuchElementException exp) {
            return 0;
        }
    }

    public boolean isPopupDisplayed(By popby) {
        try {
            WebElement popmsg_element = driver.findElement(popby);
            if (popmsg_element == null) {
                return false;
            }
            return popmsg_element.isDisplayed();
        } catch (Exception exp) {
            return false;
        }
    }

    public static void clearCookies() {
        driver.manage().deleteAllCookies();
    }

    public int getHeadingSize(By TableHeading) {
        List<WebElement> Headings = getHeadingElements(TableHeading);
        if (Headings == null || Headings.size() == 0) {
            Debugger.println("No heading elements found for the path..." + TableHeading);
            return -1;
        }
        return Headings.size();
    }

    public int getColumnIndex(By TableHeading, String column_name) {
        List<WebElement> Headings = getHeadingElements(TableHeading);
        if (Headings == null || Headings.size() == 0) {
            Debugger.println("No heading elements found for the path..." + TableHeading);
            return -1;
        }
        String heading_name = "";
        for (int index = 0; index < Headings.size(); index++) {
            heading_name = Headings.get(index).getText();
            //Debugger.println((index + 1) + ".Heading name:" + heading_name + ":...:" + column_name + ":");
            if (column_name.equalsIgnoreCase(heading_name)) {
                return index + 1;
            }
        }
        return (-1);
    }

    public int getWindowWidth() {
        return driver.manage().window().getSize().getWidth();
    }

    public void acceptAllert() {
        if (isAlertPresent()) {
            driver.switchTo().alert().accept();
        }
    }

    public void dismissAllert() {
        if (isAlertPresent()) {
            driver.switchTo().alert().dismiss();
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException ex) {
            return false;
        }
    }

    public String getPopupAlertMessage() {
        String alertmessage = "";
        try {
            alertmessage = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            return alertmessage;
        } catch (NoAlertPresentException ex) {
            return alertmessage;
        } catch (Exception exp) {
            return alertmessage;
        }
    }

    public void pageLoadTime() {
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
    }

    public static void downloadFile() throws InterruptedException {

        try {
            Robot robot = new Robot();
            robot.delay(2000);
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.delay(2000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.delay(2000);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean IsSelected(WebElement element) {
        try {
            if (element == null) {
                return false;
            }
            elementHighlight(element);
            return element.isSelected();
        } catch (Exception exp) {
            Debugger.println("Element not Selected......" + exp + "\nElement..." + element);
            return false;
        }
    }

    public void selectElement(By element) {
        try {
            WebElement webele = getElement(element);
            JavascriptExecutor javascript = (JavascriptExecutor) driver;
            javascript.executeScript("arguments[0].checked='true'", webele, "");
        } catch (Exception exp) {
            Debugger.println("Not able to select ......" + exp + "\nElement..." + element);
            throw exp;
        }
    }

    public void changeProperty(By element, String proprtyToCahnge) {
        try {
            WebElement webele = getElement(element);
            JavascriptExecutor javascript = (JavascriptExecutor) driver;
            javascript.executeScript("arguments[0]." + proprtyToCahnge, webele, "");
        } catch (Exception exp) {
            Debugger.println("Not able to select ......" + exp + "\nElement..." + element);
            throw exp;
        }
    }

    //21-12-2018
    public String compareTextWithElemntText(WebElement ele, String text) {
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        if (!ele.getText().trim().contains(text.trim())) {
            return "It does not contain your text : " + text;
        }
        return "Success";

    }

    public WebElement getSelectedElement(By element) {
        try {
            WebElement webele = driver.findElement(element);
            Select select = new Select(webele);
            WebElement selectedOption = select.getFirstSelectedOption();
            Debugger.println("selected option in drop down is : " + selectedOption.getText());
            return selectedOption;
        } catch (Exception exp) {
            Debugger.println("Not able to select ......" + exp + "\nElement..." + element);
            throw exp;
        }
    }

    public String getCurrentDate(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        String currentdate = dateFormat.format(date);
        return currentdate;
    }

    public String compareWebElementTextWithArrayText(WebElement element, String[] inputData) {
        Debugger.println("element text is : " + element.getText());
        int i;
        boolean flag = false;
        for (i = 0; i < inputData.length; i++) {
            if (inputData[i].equalsIgnoreCase(element.getText())) {
                flag = true;
                break;
            }
        }
        if (!flag)
            return "Following data is not available : " + inputData[i];


        return "Success";
    }

    public String compareTextwithArrayText(String text, String[] inputData) {
        int i;
        boolean flag = false;
        for (i = 0; i < inputData.length; i++) {
            if (inputData[i].toUpperCase().contains(text.toUpperCase().trim())) {
                flag = true;
                break;
            }
        }
        if (!flag)
            return "Following data is not available : " + inputData[i];

        return "Success";
    }

    public static String getDayOfMonthSuffix(final int n) {
        checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static WebElement getElementOfHiddenVisibility(By element) {
        try {
            sleepInSeconds(3);
            return driver.findElement(element);
        } catch (Exception exp) {
            Debugger.println("Exception is Found in get Element Of Hidden Visibility : " + exp);
            throw exp;
        }
    }

    public static void takeAScreenShot(String filename) {
        try {
            if (filename == null || filename.isEmpty()) {
                filename = "screenshot";
            }
            if (filename.indexOf(".") == -1) {
                filename = filename + ".jpg";
            }
            File screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(defaultSnapshotLocation + filename));
        } catch (Exception exp) {
        }
    }


}//end

