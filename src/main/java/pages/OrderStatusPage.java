package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderStatusPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Поле ввода номера заказа - исправленный локатор
    private By orderNumberInput = By.xpath("//input[contains(@class, 'Track_Input')]");

    // Кнопка "Посмотреть"
    private By viewButton = By.xpath("//button[text()='Посмотреть']");

    // Сообщение о том, что заказ не найден
    private By orderNotFoundMessage = By.xpath("//div[contains(@class, 'Track_NotFound')]//img");

    public OrderStatusPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private void clickElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    public OrderStatusPage enterOrderNumber(String orderNumber) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(orderNumberInput));
        input.clear();
        input.sendKeys(orderNumber);
        System.out.println("Ввели номер заказа: " + orderNumber);
        return this;
    }

    public OrderStatusPage clickViewButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(viewButton));
        clickElement(button);
        System.out.println("Нажали кнопку Посмотреть");
        return this;
    }

    public boolean isOrderNotFound() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(orderNotFoundMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getOrderNotFoundText() {
        // Сообщение может быть в другом месте
        try {
            By notFoundText = By.xpath("//div[contains(@class, 'Track_NotFound')]");
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(notFoundText));
            return element.getText();
        } catch (Exception e) {
            return "Сообщение не найдено";
        }
    }
}