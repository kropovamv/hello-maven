package tests;

import config.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.MainPage;
import pages.OrderPage;

public class OrderTests extends TestBase {

    @ParameterizedTest
    @CsvSource({
            "Иван, Петров, ул. Ленина 10, Черкизовская, +79001234567, 25.06.2026, сутки, black, Позвонить за час",
            "Мария, Иванова, ул. Пушкина 25, Сокольники, +79009876543, 26.06.2026, двое суток, grey, Домофон не работает"
    })
    @DisplayName("Оформление заказа через верхнюю кнопку")
    public void orderScooterTopButtonTest(
            String name,
            String surname,
            String address,
            String metroStation,
            String phone,
            String deliveryDate,
            String rentalPeriod,
            String scooterColor,
            String comment
    ) {
        System.out.println("========================================");
        System.out.println("Заказ через верхнюю кнопку");
        System.out.println("Данные: " + name + " " + surname + ", станция: " + metroStation);

        MainPage mainPage = new MainPage(driver);
        mainPage.clickTopOrderButton();

        OrderPage orderPage = new OrderPage(driver);
        orderPage.placeOrder(
                name, surname, address, metroStation, phone,
                deliveryDate, rentalPeriod, scooterColor, comment
        );

        String successMsg = orderPage.getSuccessMessage();
        System.out.println("Сообщение об успешном заказе: " + successMsg);
    }

    @ParameterizedTest
    @CsvSource({
            "Иван, Петров, ул. Ленина 10, Черкизовская, +79001234567, 25.06.2026, сутки, black, Позвонить за час",
            "Мария, Иванова, ул. Пушкина 25, Сокольники, +79009876543, 26.06.2026, двое суток, grey, Домофон не работает"
    })
    @DisplayName("Оформление заказа через нижнюю кнопку")
    public void orderScooterBottomButtonTest(
            String name,
            String surname,
            String address,
            String metroStation,
            String phone,
            String deliveryDate,
            String rentalPeriod,
            String scooterColor,
            String comment
    ) {
        System.out.println("========================================");
        System.out.println("Заказ через нижнюю кнопку");
        System.out.println("Данные: " + name + " " + surname + ", станция: " + metroStation);

        MainPage mainPage = new MainPage(driver);
        mainPage.clickBottomOrderButton();

        OrderPage orderPage = new OrderPage(driver);
        orderPage.placeOrder(
                name, surname, address, metroStation, phone,
                deliveryDate, rentalPeriod, scooterColor, comment
        );

        String successMsg = orderPage.getSuccessMessage();
        System.out.println("Сообщение об успешном заказе: " + successMsg);
    }
}