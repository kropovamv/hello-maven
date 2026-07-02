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

    // Локаторы для первой формы
    private final By nameInput = By.xpath("//input[@placeholder='* Имя']");
    private final By surnameInput = By.xpath("//input[@placeholder='* Фамилия']");
    private final By addressInput = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroInput = By.xpath("//input[@placeholder='* Станция метро']");
    private final By phoneInput = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath("//button[text()='Далее']");

    // Локаторы для второй формы
    private final By deliveryDateInput = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriodDropdown = By.xpath("//div[contains(@class, 'Dropdown-control')]");
    private final By commentInput = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath("//div[contains(@class, 'Order_Buttons')]/button[text()='Заказать']");

    // Локаторы для модальных окон
    private final By confirmButton = By.xpath("//button[text()='Да']");
    private final By successModalHeader = By.xpath("//div[contains(@class, 'Order_ModalHeader')]");
    private final By successModalText = By.xpath("//div[contains(@class, 'Order_Modal')]//div[contains(@class, 'Order_Text')]");

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

    // === Методы для первой формы ===

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

    public void fillCustomerForm(String name, String surname, String address, String metroStation, String phone) {
        setName(name);
        setSurname(surname);
        setAddress(address);
        selectMetroStation(metroStation);
        setPhone(phone);
        clickNextButton();
    }

    // === Методы для второй формы ===

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

    // === Методы для работы с модальными окнами ===

    public boolean isOrderSuccessfullyCreated() {
        try {
            // Ждём появления заголовка "Заказ оформлен"
            WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(successModalHeader));
            String headerText = header.getText();
            System.out.println("Заголовок модального окна: " + headerText);

            if (!headerText.contains("Заказ оформлен")) {
                return false;
            }

            // Ждём появления текста с номером заказа
            WebElement textElement = wait.until(ExpectedConditions.visibilityOfElementLocated(successModalText));
            String text = textElement.getText();
            System.out.println("Текст модального окна: " + text);

            return text.contains("Номер заказа") || text.contains("заказа");
        } catch (Exception e) {
            System.out.println("Ошибка при проверке успешного заказа: " + e.getMessage());
            return false;
        }
    }

    public String getOrderNumber() {
        try {
            WebElement textElement = wait.until(ExpectedConditions.visibilityOfElementLocated(successModalText));
            String text = textElement.getText();
            System.out.println("Текст для извлечения номера: " + text);

            // Извлекаем номер заказа из текста
            if (text.contains("Номер заказа:")) {
                String[] parts = text.split("Номер заказа:");
                if (parts.length > 1) {
                    String numberPart = parts[1].trim();
                    String[] numberParts = numberPart.split(" ");
                    if (numberParts.length > 0) {
                        return numberParts[0];
                    }
                }
            }
            return "Номер не найден";
        } catch (Exception e) {
            return "Ошибка получения номера";
        }
    }

    public void placeOrder(
            String name, String surname, String address, String metroStation, String phone,
            String deliveryDate, String rentalPeriod, String scooterColor, String comment
    ) {
        fillCustomerForm(name, surname, address, metroStation, phone);
        setDeliveryDate(deliveryDate);
        selectRentalPeriod(rentalPeriod);
        selectScooterColor(scooterColor);
        enterComment(comment);
        clickOrderButton();
        confirmOrder();
    }
}