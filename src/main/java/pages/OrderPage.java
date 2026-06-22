package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OrderPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By nameInput = By.xpath("//input[@placeholder='* Имя']");
    private final By surnameInput = By.xpath("//input[@placeholder='* Фамилия']");
    private final By addressInput = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroInput = By.xpath("//input[@placeholder='* Станция метро']");
    private final By phoneInput = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath("//button[text()='Далее']");

    private final By deliveryDateInput = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriodDropdown = By.xpath("//div[contains(@class, 'Dropdown-control')]");
    private final By commentInput = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath("//div[contains(@class, 'Order_Buttons')]/button[text()='Заказать']");
    private final By confirmButton = By.xpath("//button[text()='Да']");
    private final By successMessage = By.xpath("//div[contains(@class, 'Order_ModalHeader')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    private WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitForClickableElement(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void setName(String name) {
        WebElement element = waitForElement(nameInput);
        element.clear();
        element.sendKeys(name);
    }

    public void setSurname(String surname) {
        WebElement element = waitForElement(surnameInput);
        element.clear();
        element.sendKeys(surname);
    }

    public void setAddress(String address) {
        WebElement element = waitForElement(addressInput);
        element.clear();
        element.sendKeys(address);
    }

    public void selectMetroStation(String stationName) {
        WebElement stationInput = waitForClickableElement(metroInput);
        stationInput.click();
        stationInput.clear();
        stationInput.sendKeys(stationName);

        try {
            List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElements(
                    driver.findElements(By.xpath("//div[contains(@class, 'select-search__select')]//button[contains(@class, 'select-search__option')]"))
            ));

            boolean found = false;
            for (WebElement option : options) {
                String optionText = option.getText().trim();
                if (optionText.equals(stationName) || optionText.contains(stationName)) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
                    wait.until(ExpectedConditions.elementToBeClickable(option)).click();
                    found = true;
                    break;
                }
            }

            if (!found) {
                stationInput.sendKeys(Keys.ENTER);
            }
        } catch (Exception e) {
            stationInput.sendKeys(Keys.ENTER);
        }
    }

    public void setPhone(String phone) {
        WebElement element = waitForElement(phoneInput);
        element.clear();
        element.sendKeys(phone);
    }

    public void clickNextButton() {
        WebElement button = waitForClickableElement(nextButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    public void setDeliveryDate(String date) {
        WebElement element = waitForClickableElement(deliveryDateInput);
        element.clear();
        element.sendKeys(date);
        element.sendKeys(Keys.ENTER);
    }

    public void selectRentalPeriod(String period) {
        WebElement dropdown = waitForClickableElement(rentalPeriodDropdown);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdown);
        wait.until(ExpectedConditions.elementToBeClickable(dropdown)).click();

        By periodLocator = By.xpath("//div[contains(@class, 'Dropdown-menu')]//div[text()='" + period + "']");
        WebElement periodOption = waitForClickableElement(periodLocator);
        periodOption.click();
    }

    public void selectScooterColor(String color) {
        By colorCheckbox;
        if (color.equalsIgnoreCase("black")) {
            colorCheckbox = By.xpath("//label[@for='black']/input");
        } else {
            colorCheckbox = By.xpath("//label[@for='grey']/input");
        }

        WebElement checkbox = waitForClickableElement(colorCheckbox);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkbox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void enterComment(String comment) {
        WebElement element = waitForElement(commentInput);
        element.clear();
        element.sendKeys(comment);
    }

    public void clickOrderButton() {
        WebElement button = waitForClickableElement(orderButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    public void confirmOrder() {
        WebElement confirm = waitForClickableElement(confirmButton);
        confirm.click();
    }

    public String getSuccessMessage() {
        WebElement message = waitForElement(successMessage);
        return message.getText();
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            WebElement message = waitForElement(successMessage);
            return message.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void placeOrder(
            String name, String surname, String address, String metroStation, String phone,
            String deliveryDate, String rentalPeriod, String scooterColor, String comment
    ) {
        setName(name);
        setSurname(surname);
        setAddress(address);
        selectMetroStation(metroStation);
        setPhone(phone);
        clickNextButton();
        setDeliveryDate(deliveryDate);
        selectRentalPeriod(rentalPeriod);
        selectScooterColor(scooterColor);
        enterComment(comment);
        clickOrderButton();
        confirmOrder();
    }
}