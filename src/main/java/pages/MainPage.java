package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MainPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // === ЛОКАТОРЫ ЭЛЕМЕНТОВ ГЛАВНОЙ СТРАНИЦЫ ===

    // Кнопка "Заказать" вверху страницы
    private final By topOrderButton = By.xpath("//div[contains(@class, 'Header_Nav')]//button[text()='Заказать']");

    // Кнопка "Заказать" внизу страницы
    private final By bottomOrderButton = By.xpath("//div[contains(@class, 'Home_FinishButton')]//button[text()='Заказать']");

    // Логотип Самоката
    private final By scooterLogo = By.xpath("//img[@alt='Scooter']");

    // Логотип Яндекса
    private final By yandexLogo = By.xpath("//img[@alt='Yandex']");

    // Секция "Вопросы о важном" - ищем по тексту заголовка
    private final By faqSection = By.xpath("//div[contains(@class, 'Home_FAQ')]");

    // Все вопросы в секции FAQ
    private final By faqQuestions = By.xpath("//div[contains(@class, 'accordion__item')]//div[contains(@class, 'accordion__button')]");

    // Все панели с ответами
    private final By faqPanels = By.xpath("//div[contains(@class, 'accordion__item')]//div[contains(@class, 'accordion__panel')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private void clickElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(300);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    public MainPage open() {
        driver.get("https://qa-scooter.praktikum-services.ru/");
        return this;
    }

    public OrderPage clickTopOrderButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(topOrderButton));
        clickElement(button);
        return new OrderPage(driver);
    }

    public OrderPage clickBottomOrderButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(bottomOrderButton));
        clickElement(button);
        return new OrderPage(driver);
    }

    public MainPage clickScooterLogo() {
        WebElement logo = wait.until(ExpectedConditions.elementToBeClickable(scooterLogo));
        clickElement(logo);
        return this;
    }

    public MainPage clickYandexLogo() {
        WebElement logo = wait.until(ExpectedConditions.elementToBeClickable(yandexLogo));
        clickElement(logo);
        return this;
    }

    // ====== Метод прокручивания до FAQ ======
    public MainPage scrollToFAQ() {
        System.out.println("Прокручиваем страницу в самый низ...");

        // 1. Сначала прокручиваем в самый низ страницы
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

        try {
            Thread.sleep(1000); // Ждем загрузки контента
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Ищем секцию 'Вопросы о важном'...");

        // 2. Ищем секцию FAQ
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(faqSection));

        // 3. Прокручиваем к секции
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Секция 'Вопросы о важном' найдена");
        return this;
    }

    // Альтернативный метод scrollToFAQ с поиском по тексту
    public MainPage scrollToFAQByText() {
        System.out.println("Прокручиваем страницу в самый низ...");

        // Прокручиваем в самый низ
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Ищем секцию по тексту 'Вопросы о важном'...");

        // Ищем заголовок "Вопросы о важном"
        WebElement faqHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(), 'Вопросы о важном')]")
        ));

        // Прокручиваем к заголовку
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", faqHeader);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Секция 'Вопросы о важном' найдена");
        return this;
    }

    public MainPage clickFAQQuestion(int index) {
        List<WebElement> questions = driver.findElements(faqQuestions);

        if (questions.isEmpty()) {
            System.out.println("Вопросы не найдены! Пробуем найти вопросы другим способом...");
            // Альтернативный поиск вопросов
            questions = driver.findElements(By.xpath("//div[contains(@class, 'accordion')]//div[contains(@class, 'accordion__button')]"));
        }

        if (questions.isEmpty()) {
            throw new RuntimeException("Вопросы FAQ не найдены на странице");
        }

        System.out.println("Найдено вопросов: " + questions.size());
        System.out.println("Кликаем на вопрос " + (index + 1));

        clickElement(questions.get(index));
        return this;
    }

    public String getFAQAnswerText(int index) {
        List<WebElement> panels = driver.findElements(faqPanels);

        if (panels.isEmpty()) {
            System.out.println("Панели с ответами не найдены! Пробуем другой способ...");
            panels = driver.findElements(By.xpath("//div[contains(@class, 'accordion')]//div[contains(@class, 'accordion__panel')]"));
        }

        // Ждем, пока панель станет видимой
        wait.until(ExpectedConditions.visibilityOf(panels.get(index)));
        String text = panels.get(index).getText();
        System.out.println("Ответ на вопрос " + (index + 1) + ": " + text);
        return text;
    }

    public boolean isFAQAnswerDisplayed(int index) {
        try {
            List<WebElement> panels = driver.findElements(faqPanels);
            wait.until(ExpectedConditions.visibilityOf(panels.get(index)));
            return panels.get(index).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getFAQQuestionsCount() {
        List<WebElement> questions = driver.findElements(faqQuestions);
        return questions.isEmpty() ? 0 : questions.size();
    }
}