package com.optivem.atddaccelerator.eshop.systemtest.core.drivers.ui;

import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.UiClient;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages.HomePage;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages.NewOrderPage;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages.OrderHistoryPage;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.Driver;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UiDriver implements Driver {

    private final UiClient client;
    private final HashMap<String, String> orderNumbers;

    private HomePage homePage;
    private NewOrderPage newOrderPage;
    private OrderHistoryPage orderHistoryPage;

    private Pages currentPage;

    private static enum Pages {
        NONE,
        HOME,
        NEW_ORDER,
        ORDER_HISTORY
    }

    public UiDriver(String baseUrl) {
        this.client = new UiClient(baseUrl);
        this.orderNumbers = new HashMap<>();
    }

    @Override
    public void goToShop() {
        homePage = client.openHomePage();
        currentPage = Pages.HOME;

        newOrderPage = homePage.clickNewOrder();
    }

    private void ensureOnNewOrderPage() {
        if(currentPage != Pages.NEW_ORDER) {
            homePage = client.openHomePage();
            newOrderPage = homePage.clickNewOrder();
            currentPage = Pages.NEW_ORDER;
        }
    }

    private void ensureOnOrderHistoryPage() {
        if(currentPage != Pages.ORDER_HISTORY) {
            homePage = client.openHomePage();
            orderHistoryPage = homePage.clickOrderHistory();
            currentPage = Pages.ORDER_HISTORY;
        }
    }

    @Override
    public void placeOrder(String orderNumberAlias, String productId, String quantity) {
        ensureOnNewOrderPage();
        newOrderPage.inputProductId(productId);
        newOrderPage.inputQuantity(quantity);
        newOrderPage.clickPlaceOrder();

        var orderNumberOptional = newOrderPage.getOrderNumber();

        orderNumberOptional.ifPresent(orderNumber -> registerOrderNumber(orderNumberAlias, orderNumber));
    }

    @Override
    public void confirmOrderPlaced(String orderNumberAlias, String prefix) {
        newOrderPage.confirmConfirmationMessageShown();
        assertTrue(newOrderPage.getOrderNumber().isPresent(), "Order number should be present after placing order");
        assertTrue(newOrderPage.getTotalPrice().isPresent(), "Total price should be present after placing order");
        assertTrue(newOrderPage.getTotalPrice().get().compareTo(BigDecimal.ZERO) > 0, "Total price should be positive after placing order");

        var displayOrderNumber = newOrderPage.getOrderNumber();
        assertTrue(displayOrderNumber.isPresent(), "Order number should be present");
        assertTrue(displayOrderNumber.get().startsWith(prefix), "Order number should start with prefix: " + prefix);
    }

    @Override
    public void viewOrderDetails(String orderNumberAlias) {
        ensureOnOrderHistoryPage();
        var orderNumber = getOrderNumber(orderNumberAlias);
        orderHistoryPage.inputOrderNumber(orderNumber);
        orderHistoryPage.clickSearch();
        orderHistoryPage.waitForOrderDetails();
    }

    @Override
    public void confirmOrderDetails(String orderNumberAlias, String productId, String quantity, String status) {
        var orderNumber = getOrderNumber(orderNumberAlias);
        var displayOrderNumber = orderHistoryPage.getOrderNumber();
        assertEquals(orderNumber, displayOrderNumber, "Should display the order number: " + orderNumber);

        var displayProductId = orderHistoryPage.getProductId();
        assertEquals(productId, displayProductId, "Should display product ID: " + productId);

        var displayQuantity = orderHistoryPage.getQuantity();
        assertEquals(quantity, displayQuantity, "Should display quantity: " + quantity);

        var displayUnitPrice = orderHistoryPage.getUnitPrice();
        assertTrue(displayUnitPrice.compareTo(BigDecimal.ZERO) > 0, "Unit price should be positive");

        var displayTotalPrice = orderHistoryPage.getTotalPrice();
        assertTrue(displayTotalPrice.compareTo(BigDecimal.ZERO) > 0, "Total price should be positive");
    }


    @Override
    public void cancelOrder(String orderNumberAlias) {
        viewOrderDetails(orderNumberAlias);
        orderHistoryPage.clickCancelOrder();
    }

    @Override
    public void confirmOrderCancelled(String orderNumberAlias) {
        var displayStatusAfterCancel = orderHistoryPage.getStatus();
        assertEquals("CANCELLED", displayStatusAfterCancel, "Status should be CANCELLED after cancellation");
        orderHistoryPage.confirmCancelButtonNotVisible();
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
}
