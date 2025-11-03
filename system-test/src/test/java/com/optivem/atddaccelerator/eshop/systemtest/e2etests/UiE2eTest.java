package com.optivem.atddaccelerator.eshop.systemtest.e2etests;

import com.microsoft.playwright.*;
import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.UiClient;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages.OrderHistoryPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class UiE2eTest {
    
//    private Playwright playwright;
//    private Browser browser;
//    private Page page;
//    private String baseUrl;

    private UiClient uiClient;

    @BeforeEach
    void setUp() {
//        playwright = Playwright.create();
//        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
//        page = browser.newPage();
//        baseUrl = TestConfiguration.getBaseUrl();

        var baseUrl = TestConfiguration.getBaseUrl();
        uiClient = new UiClient(baseUrl);
    }

    @AfterEach
    void tearDown() {
//        if (page != null) {
//            page.close();
//        }
//        if (browser != null) {
//            browser.close();
//        }
//        if (playwright != null) {
//            playwright.close();
//        }

        uiClient.close();
    }

    @Test
    void shouldCalculateTotalOrderPrice() {
        // Arrange
        var homePage = uiClient.openHomePage();
        var newOrderPage = homePage.clickNewOrder();

        // Act
        newOrderPage.inputProductId("10");
        newOrderPage.inputQuantity("5");
        newOrderPage.clickPlaceOrder();
        var confirmationMessageText = newOrderPage.readConfirmationMessageText();

        var pattern = Pattern.compile("Success! Order has been created with Order Number ([\\w-]+) and Total Price \\$(\\d+(?:\\.\\d{2})?)");
        var matcher = pattern.matcher(confirmationMessageText);

        assertTrue(matcher.find(), "Confirmation message should match expected pattern. Actual: " + confirmationMessageText);

        var totalPriceString = matcher.group(2);
        var totalPrice = Double.parseDouble(totalPriceString);
        assertTrue(totalPrice > 0, "Total price should be positive. Actual: " + totalPrice);
    }

    private String createNewOrder(String productId, String quantity) {
        var homePage = uiClient.openHomePage();
        var newOrderPage = homePage.clickNewOrder();

        newOrderPage.inputProductId(productId);
        newOrderPage.inputQuantity(quantity);
        newOrderPage.clickPlaceOrder();
        var confirmationMessageText = newOrderPage.readConfirmationMessageText();

        var pattern = Pattern.compile("Success! Order has been created with Order Number ([\\w-]+)");
        var matcher = pattern.matcher(confirmationMessageText);
        assertTrue(matcher.find(), "Should extract order number from confirmation message");
        return matcher.group(1);
    }

    @Test
    void shouldRetrieveOrderHistory() {
        // Arrange
        var productId = "11";
        var quantity = "3";
        var orderNumber = createNewOrder(productId, quantity);
        var homePage = uiClient.openHomePage();
        var orderHistoryPage = homePage.clickOrderHistory();

        // Act
        orderHistoryPage.inputOrderNumber(orderNumber);
        orderHistoryPage.clickSearch();
        orderHistoryPage.waitForOrderDetails();

        var displayOrderNumber = orderHistoryPage.getOrderNumber();
        var displayProductId = orderHistoryPage.getProductId();
        var displayQuantity = orderHistoryPage.getQuantity();
        var displayUnitPrice = orderHistoryPage.getUnitPrice();
        var displayTotalPrice = orderHistoryPage.getTotalPrice();

        assertEquals(orderNumber, displayOrderNumber, "Should display the order number: " + orderNumber);
        assertEquals(productId, displayProductId, "Should display product ID 11");
        assertEquals(quantity, displayQuantity, "Should display quantity 3");
        assertTrue(displayUnitPrice.startsWith("$"), "Should display unit price with $ symbol");
        assertTrue(displayTotalPrice.startsWith("$"), "Should display total price with $ symbol");
    }

    private OrderHistoryPage viewOrderDetails(String orderNumber) {
        var homePage = uiClient.openHomePage();
        var orderHistoryPage = homePage.clickOrderHistory();

        orderHistoryPage.inputOrderNumber(orderNumber);
        orderHistoryPage.clickSearch();
        orderHistoryPage.waitForOrderDetails();
        return orderHistoryPage;
    }

    @Test
    void shouldCancelOrder() {
        // Arrange
        var productId = "12";
        var quantity = "2";
        var orderNumber = createNewOrder(productId, quantity);
        var orderHistoryPage = viewOrderDetails(orderNumber);
        var displayStatusBeforeCancel = orderHistoryPage.getStatus();
        assertEquals("PLACED", displayStatusBeforeCancel, "Initial status should be PLACED");

        // Act
        orderHistoryPage.clickCancelOrder();

        // Assert
        var displayStatusAfterCancel = orderHistoryPage.getStatus();
        assertEquals("CANCELLED", displayStatusAfterCancel, "Status should be CANCELLED after cancellation");
        orderHistoryPage.confirmCancelButtonNotVisible();
    }
}