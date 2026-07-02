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

    private final By topOrderButton = By.xpath("//div[contains(@class, 'Header_Nav')]//button[text()='Заказать']");
    private final By bottomOrderButton = By.xpath("//div[contains(@class, 'Home_FinishButton')]//button[text()='Заказать']");
    private final By scooterLogo = By.xpath("//img[@alt='Scooter']");
    private final By yandexLogo = By.xpath("//img[@alt='Yandex']");
    private final By faqSection = By.xpath("//div[contains(@class, 'Home_FAQ')]");
    private final By faqQuestions = By.xpath("//div[contains(@class, 'accordion__item')]//div[contains(@class, 'accordion__button')]");
    private final By faqPanels = By.xpath("//div[contains(@class, 'accordion__item')]//div[contains(@class, 'accordion__panel')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private void clickElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
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

    public MainPage scrollToFAQ() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(faqSection));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        return this;
    }

    public MainPage scrollToFAQByText() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        WebElement faqHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(), 'Вопросы о важном')]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", faqHeader);
        return this;
    }

    public MainPage clickFAQQuestion(int index) {
        List<WebElement> questions = driver.findElements(faqQuestions);
        if (questions.isEmpty()) {
            questions = driver.findElements(By.xpath("//div[contains(@class, 'accordion')]//div[contains(@class, 'accordion__button')]"));
        }
        clickElement(questions.get(index));
        return this;
    }

    public String getFAQAnswerText(int index) {
        List<WebElement> panels = driver.findElements(faqPanels);
        if (panels.isEmpty()) {
            panels = driver.findElements(By.xpath("//div[contains(@class, 'accordion')]//div[contains(@class, 'accordion__panel')]"));
        }
        wait.until(ExpectedConditions.visibilityOf(panels.get(index)));
        return panels.get(index).getText();
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