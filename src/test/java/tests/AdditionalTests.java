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

        // Запоминаем текущее окно
        String originalWindow = driver.getWindowHandle();
        Set<String> originalWindows = driver.getWindowHandles();

        // Кликаем на логотип Яндекса
        mainPage.clickYandexLogo();

        // Ждем открытия нового окна
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Set<String> newWindows = driver.getWindowHandles();
        newWindows.removeAll(originalWindows);

        Assertions.assertEquals(1, newWindows.size(),
                "Должно открыться новое окно. Количество новых окон: " + newWindows.size());

        // Переключаемся на новое окно
        String newWindow = newWindows.iterator().next();
        driver.switchTo().window(newWindow);

        // Проверяем URL: может быть yandex.ru или dzen.ru
        String yandexUrl = driver.getCurrentUrl();
        System.out.println("URL нового окна: " + yandexUrl);

        // Проверяем что URL содержит yandex или dzen
        Assertions.assertTrue(yandexUrl.contains("yandex") || yandexUrl.contains("dzen") || yandexUrl.contains("ya.ru"),
                "Должна открыться страница Яндекса или Дзена. URL: " + yandexUrl);

        // Закрываем новое окно и возвращаемся обратно
        driver.close();
        driver.switchTo().window(originalWindow);
    }

    @Test
    @DisplayName("Ввод неверного номера заказа должен показать сообщение 'Такого заказа нет'")
    public void invalidOrderNumberShouldShowNotFoundMessage() {
        // Открываем страницу статуса заказа напрямую
        driver.get("https://qa-scooter.praktikum-services.ru/track");

        // Ждем загрузки страницы
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Проверяем что мы на странице /track
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Текущий URL: " + currentUrl);
        Assertions.assertTrue(currentUrl.contains("/track"), "Должны быть на странице /track");

        // Находим поле ввода
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By orderInputLocator = By.xpath("//input[contains(@class, 'Track_Input')]");
        WebElement orderInput = wait.until(ExpectedConditions.visibilityOfElementLocated(orderInputLocator));
        orderInput.clear();
        orderInput.sendKeys("000000");
        System.out.println("Ввели номер заказа: 000000");

        // Нажимаем кнопку "Посмотреть"
        By viewButtonLocator = By.xpath("//button[text()='Посмотреть']");
        WebElement viewButton = wait.until(ExpectedConditions.elementToBeClickable(viewButtonLocator));
        viewButton.click();
        System.out.println("Нажали кнопку Посмотреть");

        // Ждем появления сообщения "Такого заказа нет"
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Проверяем, что отображается сообщение об ошибке
        By notFoundLocator = By.xpath("//div[contains(@class, 'Track_NotFound')]");
        boolean isNotFound = driver.findElements(notFoundLocator).size() > 0;
        Assertions.assertTrue(isNotFound, "Сообщение о том, что заказ не найден, не отображается");

        System.out.println("Тест пройден: сообщение о ненайденном заказе отображается");
    }
}