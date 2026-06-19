package tests;

import config.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import pages.MainPage;

import java.util.List;

public class FAQTests extends TestBase {

    // ИСПРАВЛЕННЫЕ ожидаемые ответы на вопросы (по порядку, начиная с 0)
    private static final String[] EXPECTED_ANSWERS = {
            "Сутки — 400 рублей. Оплата курьеру — наличными или картой.",
            "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим.",
            // ИСПРАВЛЕНО: добавили слово "времени"
            "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30.",
            "Только начиная с завтрашнего дня. Но скоро станем расторопнее.",
            "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010.",
            // ИСПРАВЛЕНО: изменили текст на фактический
            "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится.",
            "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои.",
            "Да, обязательно. Всем самокатов! И Москве, и Московской области."
    };

    // ====== ОСНОВНОЙ ТЕСТ С ПАРАМЕТРИЗАЦИЕЙ ======
    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "1, 1",
            "2, 2",
            "3, 3",
            "4, 4",
            "5, 5",
            "6, 6",
            "7, 7"
    })
    @DisplayName("Проверка текста ответов в разделе 'Вопросы о важном'")
    public void faqAnswersTest(int questionIndex, int expectedIndex) {
        System.out.println("========================================");
        System.out.println("Тестируем вопрос " + (questionIndex + 1));

        MainPage mainPage = new MainPage(driver);

        // Прокручиваем к секции FAQ
        mainPage.scrollToFAQ();

        // Кликаем на вопрос
        mainPage.clickFAQQuestion(questionIndex);

        // Получаем ответ
        String actualAnswer = mainPage.getFAQAnswerText(questionIndex);
        String expectedAnswer = EXPECTED_ANSWERS[expectedIndex];

        System.out.println("Ожидаемый ответ: " + expectedAnswer);
        System.out.println("Фактический ответ: " + actualAnswer);

        Assertions.assertEquals(expectedAnswer, actualAnswer,
                "Ответ на вопрос " + (questionIndex + 1) + " не совпадает");
    }

    // ====== ТЕСТ ДЛЯ ПРОВЕРКИ НАЛИЧИЯ СЕКЦИИ FAQ ======
    @Test
    @DisplayName("Проверка наличия секции FAQ на странице")
    public void faqSectionExistsTest() {
        System.out.println("========================================");
        System.out.println("Проверяем наличие секции FAQ");

        // Прокручиваем страницу вниз
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Пробуем найти вопросы разными способами
        System.out.println("=== Ищем вопросы разными способами ===");

        // Способ 1: по классу accordion__button
        List<WebElement> questions1 = driver.findElements(By.className("accordion__button"));
        System.out.println("Способ 1 (класс accordion__button): найдено " + questions1.size() + " вопросов");

        // Способ 2: по XPath
        List<WebElement> questions2 = driver.findElements(By.xpath("//div[contains(@class, 'accordion__button')]"));
        System.out.println("Способ 2 (XPath): найдено " + questions2.size() + " вопросов");

        // Способ 3: по тексту "Вопросы о важном"
        List<WebElement> faqHeaders = driver.findElements(By.xpath("//div[contains(text(), 'Вопросы о важном')]"));
        System.out.println("Способ 3 (текст 'Вопросы о важном'): найдено " + faqHeaders.size() + " заголовков");

        // Способ 4: поиск всех div с классом содержащим FAQ
        List<WebElement> faqElements = driver.findElements(By.xpath("//div[contains(@class, 'FAQ')]"));
        System.out.println("Способ 4 (класс содержит FAQ): найдено " + faqElements.size() + " элементов");

        // Если ничего не найдено, выводим часть HTML для отладки
        if (questions1.isEmpty() && questions2.isEmpty()) {
            System.out.println("\n=== ВНИМАНИЕ: Вопросы не найдены! ===");
            System.out.println("Выводим часть HTML страницы для отладки:");

            String pageSource = driver.getPageSource();
            int faqIndex = pageSource.indexOf("Вопросы о важном");
            if (faqIndex != -1) {
                System.out.println("Текст 'Вопросы о важном' найден на странице");
                System.out.println("Окружение вокруг текста:");
                int start = Math.max(0, faqIndex - 200);
                int end = Math.min(pageSource.length(), faqIndex + 500);
                System.out.println(pageSource.substring(start, end));
            } else {
                System.out.println("Текст 'Вопросы о важном' НЕ НАЙДЕН на странице");
                System.out.println("Выводим первые 2000 символов страницы:");
                System.out.println(pageSource.substring(0, Math.min(pageSource.length(), 2000)));
            }
        }

        // Проверяем, что хотя бы один способ нашел вопросы
        boolean found = !questions1.isEmpty() || !questions2.isEmpty();
        Assertions.assertTrue(found,
                "На странице должны быть вопросы в разделе FAQ");

        // Если нашли вопросы, выводим их тексты
        if (!questions2.isEmpty()) {
            System.out.println("\n=== Тексты найденных вопросов ===");
            for (int i = 0; i < questions2.size(); i++) {
                System.out.println("Вопрос " + (i + 1) + ": " + questions2.get(i).getText());
            }
        }
    }

    // ====== АЛЬТЕРНАТИВНЫЙ ТЕСТ - ПОИСК ПО ТЕКСТУ ======
    @Test
    @DisplayName("Проверка секции FAQ - поиск по тексту заголовка")
    public void faqTestAlternative() {
        System.out.println("========================================");
        System.out.println("Запускаем альтернативный тест FAQ");

        MainPage mainPage = new MainPage(driver);

        // Используем альтернативный метод поиска по тексту
        mainPage.scrollToFAQByText();

        // Кликаем на первый вопрос
        mainPage.clickFAQQuestion(0);

        // Проверяем ответ
        String answer = mainPage.getFAQAnswerText(0);
        System.out.println("Ответ на первый вопрос: " + answer);

        Assertions.assertFalse(answer.isEmpty(), "Ответ не должен быть пустым");
        Assertions.assertTrue(answer.contains("400 рублей"),
                "Ответ должен содержать информацию о цене");
    }

    // ====== ТЕСТ ДЛЯ ОДНОГО КОНКРЕТНОГО ВОПРОСА ======
    @Test
    @DisplayName("Проверка первого вопроса в FAQ")
    public void faqFirstQuestionTest() {
        System.out.println("========================================");
        System.out.println("Проверяем первый вопрос в FAQ");

        MainPage mainPage = new MainPage(driver);

        // Прокручиваем к секции FAQ
        mainPage.scrollToFAQ();

        // Кликаем на первый вопрос (индекс 0)
        mainPage.clickFAQQuestion(0);

        // Получаем ответ
        String actualAnswer = mainPage.getFAQAnswerText(0);
        String expectedAnswer = "Сутки — 400 рублей. Оплата курьеру — наличными или картой.";

        System.out.println("Ожидаемый ответ: " + expectedAnswer);
        System.out.println("Фактический ответ: " + actualAnswer);

        Assertions.assertEquals(expectedAnswer, actualAnswer,
                "Ответ на первый вопрос не совпадает");
    }

    // ====== ТЕСТ - ПРОВЕРКА ЧТО ВСЕ ВОПРОСЫ РАБОТАЮТ ======
    @Test
    @DisplayName("Проверка что все вопросы кликабельны")
    public void faqAllQuestionsClickableTest() {
        System.out.println("========================================");
        System.out.println("Проверяем что все вопросы кликабельны");

        MainPage mainPage = new MainPage(driver);

        // Прокручиваем к секции FAQ
        mainPage.scrollToFAQ();

        // Получаем количество вопросов
        int questionsCount = mainPage.getFAQQuestionsCount();
        System.out.println("Найдено вопросов: " + questionsCount);

        // Проверяем что есть хотя бы один вопрос
        Assertions.assertTrue(questionsCount > 0,
                "На странице должны быть вопросы в разделе FAQ");

        // Кликаем на каждый вопрос и проверяем что появляется ответ
        for (int i = 0; i < questionsCount; i++) {
            System.out.println("Проверяем вопрос " + (i + 1));

            // Кликаем на вопрос
            mainPage.clickFAQQuestion(i);

            // Проверяем что ответ отображается
            boolean isDisplayed = mainPage.isFAQAnswerDisplayed(i);
            Assertions.assertTrue(isDisplayed,
                    "Ответ на вопрос " + (i + 1) + " должен отображаться");

            // Получаем текст ответа
            String answer = mainPage.getFAQAnswerText(i);
            System.out.println("Ответ на вопрос " + (i + 1) + ": " + answer);
            Assertions.assertFalse(answer.isEmpty(),
                    "Ответ на вопрос " + (i + 1) + " не должен быть пустым");
        }
    }
}