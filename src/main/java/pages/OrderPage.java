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

    // === Методы для первой формы ===

    public void setName(String name) {
        WebElement element = waitForElement(nameInput);
        element.clear();
        element.sendKeys(name);
        System.out.println("Ввели имя: " + name);
    }

    public void setSurname(String surname) {
        WebElement element = waitForElement(surnameInput);
        element.clear();
        element.sendKeys(surname);
        System.out.println("Ввели фамилию: " + surname);
    }

    public void setAddress(String address) {
        WebElement element = waitForElement(addressInput);
        element.clear();
        element.sendKeys(address);
        System.out.println("Ввели адрес: " + address);
    }

    public void selectMetroStation(String stationName) {
        // Находим поле метро
        WebElement stationInput = waitForClickableElement(metroInput);

        // Кликаем и очищаем
        stationInput.click();
        stationInput.clear();

        // Небольшая пауза перед вводом
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Вводим название станции
        stationInput.sendKeys(stationName);
        System.out.println("Ввели название станции: " + stationName);

        // Ждем появления выпадающего списка
        try {
            Thread.sleep(1000);

            // Ищем все варианты станций в выпадающем списке
            List<WebElement> options = driver.findElements(
                    By.xpath("//div[contains(@class, 'select-search__select')]//button[contains(@class, 'select-search__option')]")
            );

            System.out.println("Найдено вариантов: " + options.size());

            boolean found = false;
            for (WebElement option : options) {
                String optionText = option.getText().trim();
                System.out.println("Вариант: " + optionText);

                if (optionText.equals(stationName)) {
                    // Скроллим и кликаем
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
                    Thread.sleep(300);
                    option.click();
                    found = true;
                    System.out.println("Выбрана станция: " + stationName);
                    break;
                }
            }

            if (!found) {
                System.out.println("Станция '" + stationName + "' не найдена в списке");
                // Пробуем найти по частичному совпадению
                for (WebElement option : options) {
                    String optionText = option.getText().trim();
                    if (optionText.contains(stationName) || stationName.contains(optionText)) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
                        Thread.sleep(300);
                        option.click();
                        found = true;
                        System.out.println("Выбрана станция (по частичному совпадению): " + optionText);
                        break;
                    }
                }

                if (!found) {
                    // Нажимаем Enter для подтверждения ввода
                    stationInput.sendKeys(Keys.ENTER);
                    System.out.println("Нажали Enter для подтверждения");
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при выборе станции: " + e.getMessage());
            stationInput.sendKeys(Keys.ENTER);
        }
    }

    public void setPhone(String phone) {
        WebElement element = waitForElement(phoneInput);
        element.clear();
        element.sendKeys(phone);
        System.out.println("Ввели телефон: " + phone);
    }

    public void clickNextButton() {
        WebElement button = waitForClickableElement(nextButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        button.click();
        System.out.println("Нажали Далее");
    }

    public void fillCustomerForm(String name, String surname, String address, String metroStation, String phone) {
        System.out.println("Начинаем заполнять первую форму...");
        setName(name);
        setSurname(surname);
        setAddress(address);
        selectMetroStation(metroStation);
        setPhone(phone);
        System.out.println("Заполнили первую форму");
    }

    // === Методы для второй формы ===

    public void setDeliveryDate(String date) {
        WebElement element = waitForClickableElement(deliveryDateInput);
        element.clear();
        element.sendKeys(date);
        element.sendKeys(Keys.ENTER);
        System.out.println("Установили дату: " + date);
    }

    public void selectRentalPeriod(String period) {
        WebElement dropdown = waitForClickableElement(rentalPeriodDropdown);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdown);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dropdown.click();

        By periodLocator = By.xpath("//div[contains(@class, 'Dropdown-menu')]//div[text()='" + period + "']");
        WebElement periodOption = waitForClickableElement(periodLocator);
        periodOption.click();
        System.out.println("Выбрали период аренды: " + period);
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
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        System.out.println("Выбрали цвет: " + color);
    }

    public void enterComment(String comment) {
        WebElement element = waitForElement(commentInput);
        element.clear();
        element.sendKeys(comment);
        System.out.println("Ввели комментарий: " + comment);
    }

    public void clickOrderButton() {
        WebElement button = waitForClickableElement(orderButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        button.click();
        System.out.println("Нажали кнопку Заказать");
    }

    public void confirmOrder() {
        WebElement confirm = waitForClickableElement(confirmButton);
        confirm.click();
        System.out.println("Подтвердили заказ");
    }

    public void fillRentalForm(String deliveryDate, String rentalPeriod, String scooterColor, String comment) {
        System.out.println("Начинаем заполнять вторую форму...");
        setDeliveryDate(deliveryDate);
        selectRentalPeriod(rentalPeriod);
        selectScooterColor(scooterColor);
        enterComment(comment);
        System.out.println("Заполнили вторую форму");
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
        fillCustomerForm(name, surname, address, metroStation, phone);
        clickNextButton();
        fillRentalForm(deliveryDate, rentalPeriod, scooterColor, comment);
        clickOrderButton();
        confirmOrder();
    }
}