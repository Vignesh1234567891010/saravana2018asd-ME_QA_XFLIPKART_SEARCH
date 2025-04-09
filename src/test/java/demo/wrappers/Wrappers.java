package demo.wrappers;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    private final WebDriver driver;
    private final WebDriverWait wait;

    public Wrappers(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToUrl(String url){
        driver.get(url);
        wait.until(ExpectedConditions.urlContains("flipkart"));
    }

    public String currentUrl(){
        return driver.getCurrentUrl();
    }


    public void click(By locator){
        wait.until(ExpectedConditions.presenceOfElementLocated(locator)).click();
    }

    public void type(By locator, String value){
        driver.findElement(locator).sendKeys(value);
    }

    public void enter(){
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.ENTER).keyUp(Keys.ENTER).perform();
    }

    public String getText(WebElement element){
        return element.getText();
    }

    public List<WebElement> getElements(By locator){
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return driver.findElements(locator);
    }

    public void wait(int timeout){
        try {
            Thread.sleep(timeout);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
