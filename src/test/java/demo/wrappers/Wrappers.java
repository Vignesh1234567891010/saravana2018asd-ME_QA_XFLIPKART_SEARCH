package demo.wrappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public void navigateToUrl(String Url){
        try {
            driver.get(Url);
            wait.until(ExpectedConditions.urlContains("flipkart"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void clickElement(WebElement element){
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }


    public WebElement getElement(By locator){
        int retries = 3;
        while (retries > 0){
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            }catch (StaleElementReferenceException e){
                System.out.println("Retrying............");
                retries--;
            }
        }
        return null;
    }

    public void clickByVisibleText(List<WebElement> elements, String visibleText){
        try {
            for (WebElement element : elements){
                if(element.getText().contains(visibleText)){
                    clickElement(element);
                    waitForPageLoad();
                    break;
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public List<WebElement> getElements(By locator){
        int retries = 3;
        while (retries > 0){
            try {
                return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            }catch (StaleElementReferenceException e){
                System.out.println("Retrying.........");
                retries--;
            }
        }
        return null;
    }

    public String getCurrentUrl(){
        try {
            return driver.getCurrentUrl();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Boolean validateUrl(String UrlContains){
        try {
            return getCurrentUrl().contains(UrlContains);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void type(WebElement element, String text){
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.clear();
        element.sendKeys(text);
        element.submit();
    }




    public int getRatingCountLessThanEqualToFour() {
        int count = 0;

        try {
            // Re-fetch the elements each time to avoid stale reference
            List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("(//div[contains(@class,'tUxRFH')]//div[@class='XQDdHH'])")));
            for (int i = 1; i <= elements.size(); i++) {
                try {
                    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("(//div[contains(@class,'tUxRFH')]//div[@class='XQDdHH'])[" + i + "]")));

                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                    waitFor(300);
                    if (element.isDisplayed()) {
                        String text = element.getText();
                        double rating = Double.parseDouble(text);
                        if (rating <= 4.0) {
                            count++;
                        }
                    }

                } catch (StaleElementReferenceException e) {
                    System.out.println("Retrying stale element at index: " + i);
                    try {
                        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("(//div[contains(@class,'tUxRFH')]//div[@class='XQDdHH'])[" + i + "]")));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                        waitFor(300);
                        String text = element.getText();
                        double rating = Double.parseDouble(text);
                        if (rating <= 4.0) {
                            count++;
                        }
                    } catch (Exception ex) {
                        System.out.println("Failed retry: " + ex.getMessage());
                    }
                }
            }

            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public void printingTitlesGreaterThanDiscount(List<WebElement> titles, List<WebElement> discounts) {
        int count = 0;

        try {
            int size = Math.min(titles.size(), discounts.size()); // safe loop boundary

            for (int i = 0; i < size; i++) {
                try {
                    WebElement discountElement = discounts.get(i);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", discountElement);

                    if (discountElement.isDisplayed()) {
                        String discountText = discountElement.getText().trim();
                        int discountValue = Integer.parseInt(discountText.replaceAll("[^0-9]", ""));
                        waitFor(300);
                        if (discountValue > 17) {
                            count++;

                            try {
                                WebElement titleElement = titles.get(i);
                                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", titleElement);
                                waitFor(300);
                                if (titleElement.isDisplayed()) {
                                    String titleText = titleElement.getText().trim();
                                    System.out.println("Title with Discount > 17 (Index " + i + "): " + titleText);
                                }
                            } catch (StaleElementReferenceException e) {
                                System.out.println("Stale Title Element at index " + i + ": " + e.getMessage());
                            }
                        }
                    }

                } catch (StaleElementReferenceException e) {
                    System.out.println("Stale Discount Element at index " + i + ": " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing discount at index " + i + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (count == 0) {
                System.out.println("Test Step: No product has a discount greater than 17%");
            }
        }
    }

    public void clickCustomerRatingByText(List<WebElement> elements, String text){
        if (elements == null || elements.isEmpty()) {
            System.out.println("Customer Rating elements list is empty or null.");
            return;
        }

        try {
            for (WebElement element : elements) {
                String title = element.getAttribute("title");
                if (title != null && title.contains(text)) {
                    WebElement checkBox = element.findElement(By.xpath(".//input[@type='checkbox']"));
                    if (checkBox.isDisplayed()) {
                        System.out.println(title);
                        clickElement(checkBox);
                        break;
                    }else {
                        System.out.println("Checkbox is not visible. Trying to click via JS...");
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();",checkBox);
                        waitForPageLoad();
                    }
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Checkbox not found for the matching element: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error while selecting Customer Rating CheckBox: " + e.getMessage());
        }
    }

    public List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        try {
            List<WebElement> elements = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//a[@class='wjcEIp']"))
            );

            JavascriptExecutor js = (JavascriptExecutor) driver;

            for (int i = 0; i < elements.size(); i++) {
                try {
                    WebElement element = elements.get(i);
                    js.executeScript("arguments[0].scrollIntoView(true);", element);
                    waitFor(300);

                    if (element.isDisplayed()) {
                        String title = element.getAttribute("title").trim();
                        titles.add(title);
                    }
                } catch (StaleElementReferenceException e) {
                    System.out.println("Retrying due to stale element at index: " + i);
                    try {
                        WebElement element = driver.findElements(By.xpath("//a[@class='wjcEIp']")).get(i);
                        js.executeScript("arguments[0].scrollIntoView(true);", element);
                        waitFor(300);

                        if (element.isDisplayed()) {
                            String title = element.getAttribute("title").trim();
                            titles.add(title);
                        }
                    } catch (Exception retryEx) {
                        System.out.println("Failed on retry: " + retryEx.getMessage());
                    }
                }
            }

            return titles;
        } catch (Exception e) {
            System.out.println("Error while getting titles: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<String> getImageUrl() {
        List<String> imageUrls = new ArrayList<>();
        try {
            List<WebElement> elements = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//img[@loading='eager']"))
            );

            JavascriptExecutor js = (JavascriptExecutor) driver;

            for (int i = 0; i < elements.size(); i++) {
                try {
                    WebElement element = elements.get(i);
                    js.executeScript("arguments[0].scrollIntoView(true);", element);
                    waitFor(300);

                    if (element.isDisplayed()) {
                        String imageUrl = element.getAttribute("src");
                        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                            imageUrls.add(imageUrl);
                        }
                    }
                } catch (StaleElementReferenceException e) {
                    System.out.println("Retrying stale element at index: " + i);
                    try {
                        WebElement element = driver.findElements(By.xpath("//img[@loading='eager']")).get(i);
                        js.executeScript("arguments[0].scrollIntoView(true);", element);
                        waitFor(300);

                        if (element.isDisplayed()) {
                            String imageUrl = element.getAttribute("src");
                            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                                imageUrls.add(imageUrl);
                            }
                        }
                    } catch (Exception retryEx) {
                        System.out.println("Retry failed: " + retryEx.getMessage());
                    }
                }
            }

            return imageUrls;
        } catch (Exception e) {
            System.out.println("Error while getting image URLs: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Integer> getReviewCounts() {
        List<Integer> reviewCounts = new ArrayList<>();
        try {
            List<WebElement> elements = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(
                            By.xpath("//span[contains(text(),'(') and contains(text(),')')]"))
            );

            JavascriptExecutor js = (JavascriptExecutor) driver;

            for (int i = 0; i < elements.size(); i++) {
                try {
                    WebElement element = elements.get(i);
                    js.executeScript("arguments[0].scrollIntoView(true);", element);
                    waitFor(300);
                    if (element.isDisplayed()) {
                        String reviewText = element.getText().replaceAll("[^0-9]", "");
                        if (!reviewText.isEmpty()) {
                            reviewCounts.add(Integer.parseInt(reviewText));
                        }
                    }

                } catch (StaleElementReferenceException e) {
                    System.out.println("Retrying stale element at index: " + i);
                    try {
                        WebElement element = driver.findElements(By.xpath("//span[contains(text(),'(') and contains(text(),')')]")).get(i);
                        js.executeScript("arguments[0].scrollIntoView(true);", element);
                        waitFor(300);
                        if (element.isDisplayed()) {
                            String reviewText = element.getText().replaceAll("[^0-9]", "");
                            if (!reviewText.isEmpty()) {
                                reviewCounts.add(Integer.parseInt(reviewText));
                            }
                        }
                    } catch (Exception retryEx) {
                        System.out.println("Retry failed: " + retryEx.getMessage());
                    }
                }
            }

            return reviewCounts;
        } catch (Exception e) {
            System.out.println("Error while getting review counts: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Integer> sortIndicesForReviewCount(List<Integer> values){
        try {
            return IntStream.range(0,values.size())
                    .boxed()
                    .sorted((i,j)->values.get(j)-values.get(i))
                    .limit(5)
                    .collect(Collectors.toList());
        }catch (Exception e){
            System.out.println("Error while sorting: "+e.getMessage());
            return null;
        }
    }


    public void waitForPageLoad(){
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }


    public void waitFor(int millis){
        try {
            Thread.sleep(millis);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
