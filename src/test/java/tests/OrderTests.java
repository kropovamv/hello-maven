package tests;

import config.TestBase;
import org.junit.jupiter.api.Assertions;
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
        MainPage mainPage = new MainPage(driver);
        mainPage.clickTopOrderButton();

        OrderPage orderPage = new OrderPage(driver);
        orderPage.placeOrder(
                name, surname, address, metroStation, phone,
                deliveryDate, rentalPeriod, scooterColor, comment
        );

        // ✅ Проверяем, что заказ действительно оформлен
        boolean isSuccess = orderPage.isOrderSuccessfullyCreated();
        Assertions.assertTrue(isSuccess, "Заказ не был оформлен успешно");

        // ✅ Дополнительно проверяем, что номер заказа получен
        String orderNumber = orderPage.getOrderNumber();
        Assertions.assertNotNull(orderNumber, "Номер заказа не получен");
        Assertions.assertFalse(orderNumber.isEmpty(), "Номер заказа пустой");
        System.out.println("Заказ успешно оформлен! Номер: " + orderNumber);
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
        MainPage mainPage = new MainPage(driver);
        mainPage.clickBottomOrderButton();

        OrderPage orderPage = new OrderPage(driver);
        orderPage.placeOrder(
                name, surname, address, metroStation, phone,
                deliveryDate, rentalPeriod, scooterColor, comment
        );

        // ✅ Проверяем, что заказ действительно оформлен
        boolean isSuccess = orderPage.isOrderSuccessfullyCreated();
        Assertions.assertTrue(isSuccess, "Заказ не был оформлен успешно");

        // ✅ Дополнительно проверяем, что номер заказа получен
        String orderNumber = orderPage.getOrderNumber();
        Assertions.assertNotNull(orderNumber, "Номер заказа не получен");
        Assertions.assertFalse(orderNumber.isEmpty(), "Номер заказа пустой");
        System.out.println("Заказ успешно оформлен! Номер: " + orderNumber);
    }
}