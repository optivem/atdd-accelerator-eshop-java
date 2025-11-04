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
    public void placeOrder(String order, String productId, String quantity) {
        ensureOnNewOrderPage();
        newOrderPage.inputProductId(productId);
        newOrderPage.inputQuantity(quantity);
        newOrderPage.clickPlaceOrder();

        var orderNumberOptional = newOrderPage.getOrderNumber();

        orderNumberOptional.ifPresent(orderNumber -> registerOrderNumber(order, orderNumber));
    }

    @Override
    public void confirmOrderPlaced(String order, String prefix) {
        newOrderPage.confirmConfirmationMessageShown();
        assertTrue(newOrderPage.getOrderNumber().isPresent(), "Order number should be present after placing order");
        assertTrue(newOrderPage.getTotalPrice().isPresent(), "Total price should be present after placing order");
        assertTrue(newOrderPage.getTotalPrice().get().compareTo(BigDecimal.ZERO) > 0, "Total price should be positive after placing order");

        var displayOrderNumber = newOrderPage.getOrderNumber();
        assertTrue(displayOrderNumber.isPresent(), "Order number should be present");
        assertTrue(displayOrderNumber.get().startsWith(prefix), "Order number should start with prefix: " + prefix);
    }

    @Override
    public void viewOrderDetails(String order) {
        ensureOnOrderHistoryPage();
        var orderNumber = getOrderNumber(order);
        orderHistoryPage.inputOrderNumber(orderNumber);
        orderHistoryPage.clickSearch();
        orderHistoryPage.waitForOrderDetails();
    }

    @Override
    public void confirmOrderDetails(String order, String productId, String quantity, String status) {
        // Navigate to order details if not already there
        if (orderHistoryPage == null) {
            viewOrderDetails(order);
        }

        var orderNumber = getOrderNumber(order);
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

        var displayStatus = orderHistoryPage.getStatus();
        assertEquals(status, displayStatus, "Should display status: " + status);
    }


    @Override
    public void cancelOrder(String order) {
        viewOrderDetails(order);
        orderHistoryPage.clickCancelOrder();
    }

    @Override
    public void confirmOrderCancelled(String order) {
        var displayStatusAfterCancel = orderHistoryPage.getStatus();
        assertEquals("CANCELLED", displayStatusAfterCancel, "Status should be CANCELLED after cancellation");
        orderHistoryPage.confirmCancelButtonNotVisible();
    }

    @Override
    public void confirmOrderStatusIsCancelled(String order) {
        // Navigate to order details if not already there
        if (orderHistoryPage == null) {
            viewOrderDetails(order);
        }

        var displayStatusAfterCancel = orderHistoryPage.getStatus();
        assertEquals("CANCELLED", displayStatusAfterCancel, "Status should be CANCELLED after cancellation");
        orderHistoryPage.confirmCancelButtonNotVisible();
    }


    @Override
    public void confirmOrderPlacementFailed(String order, String errorMessage) {
        // For UI driver, check if the confirmation page shows an error message
        // or if we're still on the new order page with an error displayed
        var pageErrorMessage = newOrderPage.readConfirmationMessageText();
        assertTrue(pageErrorMessage.contains(errorMessage),
                   "Expected error message to contain: " + errorMessage + ", but got: " + pageErrorMessage);
    }

    private void registerOrderNumber(String order, String orderNumber) {
        if(orderNumbers.containsKey(order)) {
            throw new IllegalStateException("Order number alias already registered: " + order);
        }

        orderNumbers.put(order, orderNumber);
    }

    private String getOrderNumber(String order) {
        var orderNumber = orderNumbers.get(order);
        if(orderNumber == null) {
            throw new IllegalStateException("Order number alias not registered: " + order);
        }

        return orderNumber;
    }

    @Override
    public void close() {
        client.close();
    }
}
