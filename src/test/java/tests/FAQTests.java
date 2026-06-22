package tests;

import config.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.MainPage;

public class FAQTests extends TestBase {

    private static final String[] EXPECTED_ANSWERS = {
            "Сутки — 400 рублей. Оплата курьеру — наличными или картой.",
            "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим.",
            "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30.",
            "Только начиная с завтрашнего дня. Но скоро станем расторопнее.",
            "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010.",
            "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится.",
            "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои.",
            "Да, обязательно. Всем самокатов! И Москве, и Московской области."
    };

    @ParameterizedTest
    @CsvSource({
            "0, 0", "1, 1", "2, 2", "3, 3",
            "4, 4", "5, 5", "6, 6", "7, 7"
    })
    @DisplayName("Проверка текста ответов в разделе 'Вопросы о важном'")
    public void faqAnswersTest(int questionIndex, int expectedIndex) {
        MainPage mainPage = new MainPage(driver);
        mainPage.scrollToFAQ();
        mainPage.clickFAQQuestion(questionIndex);

        String actualAnswer = mainPage.getFAQAnswerText(questionIndex);
        String expectedAnswer = EXPECTED_ANSWERS[expectedIndex];

        Assertions.assertEquals(expectedAnswer, actualAnswer,
                "Ответ на вопрос " + (questionIndex + 1) + " не совпадает");
    }
}