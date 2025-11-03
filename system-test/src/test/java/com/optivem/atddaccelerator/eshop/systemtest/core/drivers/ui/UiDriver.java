package com.optivem.atddaccelerator.eshop.systemtest.core.drivers.ui;

import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.UiClient;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages.HomePage;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages.NewOrderPage;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages.OrderHistoryPage;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.Driver;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UiDriver implements Driver {

    private final UiClient client;
    private final HashMap<String, String> orderNumbers;

    private HomePage homePage;
    private NewOrderPage newOrderPage;
    private OrderHistoryPage orderHistoryPage;


    public UiDriver(String baseUrl) {
        this.client = new UiClient(baseUrl);
        this.orderNumbers = new HashMap<>();
    }

    @Override
    public void goToShop() {
        homePage = client.openHomePage();
        newOrderPage = homePage.clickNewOrder();
    }

    @Override
    public void placeOrder(String orderNumberAlias, String productId, String quantity) {
        newOrderPage.inputProductId(productId);
        newOrderPage.inputQuantity(quantity);
        newOrderPage.clickPlaceOrder();

        var orderNumberOptional = newOrderPage.getOrderNumber();

        orderNumberOptional.ifPresent(orderNumber -> registerOrderNumber(orderNumberAlias, orderNumber));
    }

    @Override
    public void confirmOrderCreated(String orderNumberAlias) {
        newOrderPage.confirmConfirmationMessage();
    }

    @Override
    public void viewOrderDetails(String orderNumberAlias) {
        var orderNumber = getOrderNumber(orderNumberAlias);
        orderHistoryPage.inputOrderNumber(orderNumber);
        orderHistoryPage.clickSearch();
        orderHistoryPage.waitForOrderDetails();
    }

    @Override
    public void confirmOrderDetailsExist(String orderNumberAlias) {
        orderHistoryPage.waitForOrderDetails();
    }


    @Override
    public void cancelOrder(String orderNumberAlias) {

    }

    @Override
    public void confirmOrderCancelled(String orderNumberAlias) {

    }

    @Override
    public void confirmOrderNumberGenerated(String orderNumberAlias) {

    }

    @Override
    public void confirmOrderNumberStartsWith(String orderNumberAlias, String prefix) {
        var orderNumber = getOrderNumber(orderNumberAlias);
        var displayOrderNumber = orderHistoryPage.getOrderNumber();
        assertTrue(displayOrderNumber.startsWith(prefix), "Order number should start with prefix: " + prefix);
    }

    @Override
    public void confirmOrderDetailsHaveOrderNumber(String orderNumberAlias) {
        var orderNumber = getOrderNumber(orderNumberAlias);
        var displayOrderNumber = orderHistoryPage.getOrderNumber();
        assertEquals(orderNumber, displayOrderNumber, "Should display the order number: " + orderNumber);
    }

    @Override
    public void confirmOrderDetailsHaveProductId(String orderNumberAlias, String productId) {
        var displayProductId = orderHistoryPage.getProductId();
        assertEquals(productId, displayProductId, "Should display product ID: " + productId);
    }

    @Override
    public void confirmOrderDetailsHaveQuantity(String orderNumberAlias, String quantity) {
        var displayQuantity = orderHistoryPage.getQuantity();
        assertEquals(quantity, displayQuantity, "Should display quantity: " + quantity);
    }

    @Override
    public void confirmOrderDetailsHavePositiveUnitPrice(String orderNumberAlias) {
        var displayUnitPrice = orderHistoryPage.getUnitPrice();
        var decimalUnitPrice = new BigDecimal(displayUnitPrice);
        assertTrue(decimalUnitPrice.compareTo(BigDecimal.ZERO) > 0, "Unit price should be positive");
    }

    @Override
    public void confirmOrderDetailsHavePositiveTotalPrice(String orderNumberAlias) {
        var displayTotalPrice = orderHistoryPage.getTotalPrice();
        var decimalTotalPrice = new BigDecimal(displayTotalPrice);
        assertTrue(decimalTotalPrice.compareTo(BigDecimal.ZERO) > 0, "Total price should be positive");


        var totalPriceString = orderHistoryPage.getTotalPrice();
        var totalPrice = Double.parseDouble(totalPriceString);
        assertTrue(totalPrice > 0, "Total price should be positive. Actual: " + totalPrice);
    }

    @Override
    public void confirmOrderStatusIsCancelled(String orderNumberAlias) {
        var displayStatusAfterCancel = orderHistoryPage.getStatus();
        assertEquals("CANCELLED", displayStatusAfterCancel, "Status should be CANCELLED after cancellation");
        orderHistoryPage.confirmCancelButtonNotVisible();
    }

    private void registerOrderNumber(String orderNumberAlias, String orderNumber) {
        if(orderNumbers.containsKey(orderNumberAlias)) {
            throw new IllegalStateException("Order number alias already registered: " + orderNumberAlias);
        }

        orderNumbers.put(orderNumberAlias, orderNumber);
    }

    private String getOrderNumber(String orderNumberAlias) {
        var orderNumber = orderNumbers.get(orderNumberAlias);
        if(orderNumber == null) {
            throw new IllegalStateException("Order number alias not registered: " + orderNumberAlias);
        }

        return orderNumber;
    }




    @Override
    public void close() {
        client.close();
    }

    /*



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

     */
}
