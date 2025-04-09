package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        System.out.println("Start TestCase: TestCase1");
        action = new Wrappers(driver);
        action.navigateToUrl("https://www.flipkart.com");
        String currentUrl = action.currentUrl();

        if(currentUrl.contains("flipkart")){
            Assert.assertTrue(true);
            System.out.println("Test Case 1: Navigated to the flipkart");
        }else {
            Assert.assertFalse(false);
            System.out.println("Test Case 1: Unable to Navigate to flipkart");
        }

        By searchBox = By.name("q");
        action.type(searchBox, input);
        String inputText = driver.findElement(By.name("q")).getAttribute("value");
        if(inputText.contains(input)){
            Assert.assertTrue(true);
            System.out.println("Test Case 1: Correct input is entered");
        }
        action.enter();
        action.wait(2000);
        System.out.println("Test Case 1: Searched "+input);

        By popularity = By.xpath("//span[text()='Sort By']//following-sibling::div[text()='Popularity']");
        action.click(popularity);
        action.wait(2000);

        System.out.println("Test Case 1: Clicked On Popularity Sort By");
        By ratingsElements = By.xpath("//div[@class='XQDdHH']");

        List<WebElement> ratings = action.getElements(ratingsElements);
        int count = 0;
        for (WebElement ratingElement : ratings){
            String ratingText = action.getText(ratingElement);
            try {
                double rating = Double.parseDouble(ratingText);
                if(rating <= 4.0){
                    count++;
                    System.out.println("Valid Rating Found: "+rating);
                }
            }catch (NumberFormatException ignored){
                System.out.println("Skipped non-numeric rating: " + ratingText);
            }
        }

        System.out.println("Total products with rating <= 4.0: " + count);
        System.out.println("End Test: TestCase1");

    }

    @Test(description = "Search IPhone")
    @Parameters("input2")
    public void testCase02(String input){

        System.out.println("Start TestCase: TestCase2");

        action = new Wrappers(driver);
        action.navigateToUrl("https://www.flipkart.com");
        System.out.println("Test Case 2: Navigated to FlipKart");

        By searchBox = By.name("q");
        action.type(searchBox, input);
        String inputText = driver.findElement(searchBox).getAttribute("value");
        if(inputText.contains(input)){
            Assert.assertTrue(true);
            System.out.println("Test Case 2: Correct input is entered");
        }
        action.enter();
        action.wait(2000);
        System.out.println("Test Case 2: Searched "+input);

        By titleElements = By.xpath("//div[@class='KzDlHZ']");
        By discountElements = By.xpath("//span[contains(text(),'% off')]");

        List<WebElement> titles = action.getElements(titleElements);
        List<WebElement> discounts = action.getElements(discountElements);

        int count = 0;
        for(int i=0;i < Math.min(titles.size(), discounts.size());i++){

            WebElement discountElement = discounts.get(i);
            String discountText = action.getText(discountElement);

            try {
                int discountValue = Integer.parseInt(discountText.replaceAll("[^0-9]",""));

                if(discountValue > 17){
                    count++;
                    String title = action.getText(titles.get(i));
                    System.out.println("Title: "+title+" and its discount is: "+discountValue+"% off");
                }
            }catch (NumberFormatException ignored){

            }
        }
        System.out.println("count: "+count);
        System.out.println("End Test: TestCase2");
    }

    @Test(description = "Search Coffee Mug")
    public void testCase03(){

        System.out.println("Start TestCase: TestCase2");
        action = new Wrappers(driver);
        action.navigateToUrl("https://www.flipkart.com");
        System.out.println("Test Case 3: Navigated to flipkart");

        By searchBox = By.name("q");
        action.type(searchBox, "Coffee Mug");
        String inputText = driver.findElement(searchBox).getAttribute("value");
        if(inputText.contains("Coffee Mug")){
            System.out.println("Test Case 3: Correct Input is entered");
        }
        action.enter();
        System.out.println("Test Case 3: Searched Coffee Mug");

        action.wait(2000);

        By ratingFilter = By.xpath("//div[contains(text(),'4★')]");
        action.click(ratingFilter);
        System.out.println("Test Case 3: Clicked 4 & above rating");

        action.wait(2000);
        class Product{
            final String title;
            final String imageUrl;
            final int reviewCount;

            public Product(String title, String imageUrl, int reviewCount){
                this.title = title;
                this.imageUrl = imageUrl;
                this.reviewCount = reviewCount;
            }
        }

        List<WebElement> productCard = action.getElements(By.xpath("//div[@class='slAVV4']"));
        List<Product> coffeeMug = new ArrayList<>();
        Set<String> seenTitles = new HashSet<>();

        for (WebElement product : productCard){
            try {
                String title = product.findElement(By.xpath(".//a[contains(@class,'wjcEIp')]")).getText();

                //Avoid duplicated
                if(seenTitles.contains(title)){
                    continue;
                }
                seenTitles.add(title);
                String imageUrl = product.findElement(By.xpath(".//img[@class='DByuf4']")).getAttribute("src");
                String reviewText = product.findElement(By.xpath(".//span[@class='Wphh3N']")).getText();

                int reviewCount = 0;
                if(reviewText.matches("\\([\\d,]+\\)")){
                    reviewCount = Integer.parseInt(reviewText.replaceAll("[(),]",""));
                }

                coffeeMug.add(new Product(title, imageUrl, reviewCount));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        coffeeMug.sort((a,b) -> Integer.compare(b.reviewCount, a.reviewCount));

        System.out.println("Test Case 3: Top 5 Coffee Mugs (By Reviews)");
        for (int i=0; i < Math.min(5, coffeeMug.size()); i++){
            Product p = coffeeMug.get(i);
            System.out.println("Test Case 3: Title= "+p.title);
            System.out.println("Test Case 3: Image Url= "+p.imageUrl);
            System.out.println("Test Case 3: Review Count= "+p.reviewCount);
            System.out.println("------------------------------------------");
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
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }


    @AfterTest
    public void endTest()  {

        driver.close();
        driver.quit();

    }
}