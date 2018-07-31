package first;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Monika on 30.07.2018.
 */
public class MyTest {

    private static final String KEYWORD = "test";
    private static final String LOCATION_KRAKOW = "Krakow, Poland";
    private static final String SW_DEV_IN_TEST = "Software Development Engineer in Test";
    private static final String SENIOR_SW_DEV_IN_TEST = "Senior Software Development Engineer in Test";

    public WebDriver driver;

    public JavascriptExecutor js;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get("https://akamaijobs.referrals.selectminds.com/");

        //Type 'test' in keyword field
        WebElement elKeyword = driver.findElement(By.id("keyword"));
        verifyElementVisible(elKeyword);
        elKeyword.sendKeys(KEYWORD);

        //Find location field
        WebElement location = driver.findElement(By.id("loc_placeholder"));
        verifyElementVisible(location);
        location.click();

        //Use JavascriptExecutor to make the element visible
        isjQueryLoaded(driver);
        js.executeScript("jQuery('#location_facet').css('display','block')");
        Select select = new Select(driver.findElement(By.id("location_facet")));
        select.selectByVisibleText(LOCATION_KRAKOW);

        //Submit search
        WebElement search = driver.findElement(By.id("jSearchSubmit"));
        verifyElementVisible(search);
        search.click();
    }

    @Test
    public void verifyAkamaiCareersWebsite() throws Exception {

        //Validate number of results
        WebElement results = ((ChromeDriver) driver).findElementByXPath("//*[@id=\"jobs_filters_title\"]/div/span[1]");
        verifyElementVisible(results);
        Thread.sleep(3000);
        System.out.println("Number of job results are " + results.getText());
        //Assert.assertTrue("Something gone not well. Cannot verify number of results.", results.getText().contains("15"));

        //Validate "Software Development Engineer in Test" appears just once
        System.out.println("'Software Development Engineer in Test' appears on website " + isTextPresent(SW_DEV_IN_TEST) + " time.");
        Assert.assertTrue("'Software Development Engineer in Test' appears once the website.",isTextPresent(SW_DEV_IN_TEST) > 0);

        //Validate "Senior Software Development Engineer in Test" job offer exists and check posting date
        Assert.assertTrue("Not found 'Senior Software Development Engineer in Test's' offer on that page.",isTextPresent(SENIOR_SW_DEV_IN_TEST) > 0);
        WebElement seniorElem = ((ChromeDriver) driver).findElementByXPath("//*[@id=\"job_list_9186\"]/div/div[1]/p[1]/a");
        verifyElementVisible(seniorElem);
        seniorElem.click();
        WebElement date = ((ChromeDriver) driver).findElementByXPath("//*[@id=\"description_box\"]/div[1]/div[1]/dl[2]/dd/span[1]");
        verifyElementVisible(date);
        System.out.println("Date of posting Senior's offer is: " + date.getText());
        //Assert.assertTrue("Date of the posted offer is different.", date.getText().contains("1 day ago"));

        driver.close();
    }

    public int isTextPresent(String text) {
        List<WebElement> foundElements = driver.findElements(By.partialLinkText(text));
        return foundElements.size();
    }

    public void verifyElementVisible(WebElement element) {
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(element));
    }

    public void isjQueryLoaded(WebDriver driver) {
        System.out.println("Waiting for ready state complete");
        (new WebDriverWait(driver, 30)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                JavascriptExecutor js = (JavascriptExecutor) d;
                String readyState = js.executeScript("return document.readyState").toString();
                System.out.println("Ready State: " + readyState);
                return (Boolean) js.executeScript("return !!window.jQuery && window.jQuery.active == 0");
            }
        });
    }
}
