package tests;

import config.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.MainPage;

import java.time.Duration;
import java.util.Set;

public class AdditionalTests extends TestBase {

    @Test
    @DisplayName("Клик на логотип Самоката должен вести на главную страницу")
    public void scooterLogoShouldNavigateToMainPage() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();

        String expectedUrl = "https://qa-scooter.praktikum-services.ru/";
        Assertions.assertEquals(expectedUrl, driver.getCurrentUrl(),
                "Начальный URL не соответствует ожидаемому");

        mainPage.clickScooterLogo();

        String newUrl = driver.getCurrentUrl();
        Assertions.assertTrue(newUrl.contains("qa-scooter.praktikum-services.ru"),
                "Логотип Самоката не ведет на главную страницу. URL: " + newUrl);
    }

    @Test
    @DisplayName("Клик на логотип Яндекса должен открыть новое окно с Яндексом")
    public void yandexLogoShouldOpenInNewWindow() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();

        String originalWindow = driver.getWindowHandle();
        Set<String> originalWindows = driver.getWindowHandles();

        mainPage.clickYandexLogo();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        Set<String> newWindows = driver.getWindowHandles();
        newWindows.removeAll(originalWindows);

        Assertions.assertEquals(1, newWindows.size(),
                "Должно открыться новое окно");

        String newWindow = newWindows.iterator().next();
        driver.switchTo().window(newWindow);

        String yandexUrl = driver.getCurrentUrl();
        Assertions.assertTrue(yandexUrl.contains("yandex") || yandexUrl.contains("dzen") || yandexUrl.contains("ya.ru"),
                "Должна открыться страница Яндекса или Дзена. URL: " + yandexUrl);

        driver.close();
        driver.switchTo().window(originalWindow);
    }

    @Test
    @DisplayName("Ввод неверного номера заказа должен показать сообщение 'Такого заказа нет'")
    public void invalidOrderNumberShouldShowNotFoundMessage() {
        driver.get("https://qa-scooter.praktikum-services.ru/track");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        By orderInputLocator = By.xpath("//input[contains(@class, 'Track_Input')]");
        WebElement orderInput = wait.until(ExpectedConditions.visibilityOfElementLocated(orderInputLocator));
        orderInput.clear();
        orderInput.sendKeys("000000");

        By viewButtonLocator = By.xpath("//button[text()='Посмотреть']");
        WebElement viewButton = wait.until(ExpectedConditions.elementToBeClickable(viewButtonLocator));
        viewButton.click();

        By notFoundLocator = By.xpath("//div[contains(@class, 'Track_NotFound')]");
        boolean isNotFound = wait.until(ExpectedConditions.visibilityOfElementLocated(notFoundLocator)).isDisplayed();

        Assertions.assertTrue(isNotFound, "Сообщение о том, что заказ не найден, не отображается");
    }
}