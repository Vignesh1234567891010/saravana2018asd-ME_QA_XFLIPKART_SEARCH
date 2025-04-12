package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;


// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    WebDriver driver;
    Wrappers action;

    /*
     * TODO: Write your tests here with testng @Test annotation. 
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */
    @Test(description = "Search Washing Machine")
    @Parameters("input1")
    public void testCase01(String input){

        System.out.println("Start Test Case: TestCase01");

        //Creating Object for Wrapper Class and Passing driver reference
        action = new Wrappers(driver);

        //Navigate to flipkart
        action.navigateToUrl("https://www.flipkart.com");

        //Validating url
        Assert.assertTrue(action.validateUrl("flipkart"), "Could not Navigate to the Url");
        System.out.println("Test Step: Successfully Navigated to the Url");
        action.waitForPageLoad();

        //Locating WebElement of SearchBox
        WebElement searchBox = action.getElement(By.name("q"));

        //Entering the Search input in the Field and Submitting
        action.type(searchBox, input);
        System.out.println("Test Step: Successfully Searched "+input);

        //Waiting for PageLoad
        action.waitForPageLoad();


        //Click The Element Using Visible Text in the Sort By
        action.clickByVisibleText(action.getElements(By.xpath("//div[@class='sHCOk2']//child::div")), "Popularity");
        System.out.println("Test Step: Successfully Clicked on Popularity Sort");

        //Waiting for PageLoad
        action.waitForPageLoad();


        //Getting count of Product less than equal to 4.0 rating
        int count = action.getRatingCountLessThanEqualToFour();
        System.out.println("Test Step: Count of Product Less Than Equal to 4.0: "+count);
        System.out.println("End Test Case");

    }

    @Test(description = "Search iPhone")
    @Parameters("input2")
    public void testCase02(String input){

        System.out.println("Start Test Case: TestCase02");

        //Creating Object for Wrapper Class and Passing driver reference
        action = new Wrappers(driver);

        //Navigate to FlipKart
        action.navigateToUrl("https://www.flipkart.com");

        //Validating Url
        Assert.assertTrue(action.validateUrl("flipkart"), "Could not navigate to URL");
        System.out.println("Test Step: Successfully Navigated to the Url");
        action.waitForPageLoad();

        //Locating WebElement for searchBox
        WebElement searchBox = action.getElement(By.name("q"));

        //Entering the search input in the searchBox and submitting
        action.type(searchBox, input);
        Assert.assertTrue(action.validateUrl(input), "Unbale to search");
        System.out.println("Test Step: Successfully Searched "+input);

        action.waitForPageLoad();

        //Getting List Of WebElements for titles and discount
        List<WebElement> titles = action.getElements(By.xpath("//div[@class='KzDlHZ']"));
        List<WebElement> discounts = action.getElements(By.xpath("//div[@class='KzDlHZ']//following::div[@class='UkUFwK']//span"));

        //Printing Title Greater than 17 discount
        action.printingTitlesGreaterThanDiscount(titles, discounts);
        System.out.println("End Test Case");

    }

    @Test(description = "Search Coffee Mug")
    @Parameters("input3")
    public void testCase03(String input){

        System.out.println("Start Test Case: TestCase03");

        //Creating Wrapper class object and passing driver as reference
        action = new Wrappers(driver);

        //Navigate to flipkart
        action.navigateToUrl("https://www.flipkart.com");

        //Validate Url
        Assert.assertTrue(action.validateUrl("flipkart"),"Test Step: Unable to Navigate to flipkart");
        System.out.println("Test Step: Successfully Navigated to flipkart");
        action.waitForPageLoad();

        //Locating WebElement for search box
        WebElement searchBox = action.getElement(By.name("q"));

        //Entering Search input in the Search field and submitting
        action.type(searchBox, input);
        action.waitForPageLoad();
        System.out.println("Test Step: Successfully Searched "+input);

        //Locating List Of WebElement for Customer Ratings
        List<WebElement> customerRatings = action.getElements(By.xpath("(//div[contains(@class,'ewzVkT')])[position()<=4]"));

        //Selecting Check Box for Customer Rating
        action.clickCustomerRatingByText(customerRatings, "4");
        action.waitForPageLoad();

        //Extracting Text from WebElements
        List<String> titlesText = action.getTitles();

        //Extracting Image Url from WebElements
        List<String> imageUrls = action.getImageUrl();

        //Extracting Review Count from Welement
        List<Integer> reviewCounts = action.getReviewCounts();

        //Extracting indices after sorting
        List<Integer> sortedIndices = action.sortIndicesForReviewCount(reviewCounts);

        System.out.println("Test Step: Top 5 Coffee Mugs (4★ & above) by Review Count: ");
        for (int index : sortedIndices){
            System.out.println("Title: "+titlesText.get(index));
            System.out.println("Image Url: "+imageUrls.get(index));
            System.out.println("Reviews: "+reviewCounts.get(index));
            System.out.println("-----------------------------------------------------------------------------------------");
        }
    }




     
    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */
    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().window().maximize();
    }


    @AfterTest
    public void endTest()  {

        driver.close();
        driver.quit();

    }
}