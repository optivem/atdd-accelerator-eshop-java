package com.optivem.atddaccelerator.eshop.systemtest.core.drivers.api;

import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.ApiClient;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.Driver;

import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ApiDriver implements Driver {

    private final ApiClient apiClient;

    private final HashMap<String, String> orderNumbers;
    private final HashMap<String, HttpResponse<String>> ordersPlaced;
    private final HashMap<String, HttpResponse<String>> ordersViewed;
    private final HashMap<String, HttpResponse<String>> ordersCancelled;

    public ApiDriver(String baseUrl) {
        this.apiClient = new ApiClient(baseUrl);
        this.orderNumbers = new HashMap<>();
        this.ordersPlaced = new HashMap<>();
        this.ordersViewed = new HashMap<>();
        this.ordersCancelled = new HashMap<>();
    }

    @Override
    public void goToShop() {
        var httpResponse = apiClient.getEchoController().echo();
        apiClient.getEchoController().confirmEchoSuccessful(httpResponse);
    }

    @Override
    public void placeOrder(String orderNumberAlias, String productId, String quantity) {
        var httpResponse = apiClient.getOrderController().placeOrder(productId, quantity);
        registerOrderResponse(ordersPlaced, orderNumberAlias, httpResponse);

        var orderNumberOptional = apiClient.getOrderController().getOrderNumberIfOrderPlacedSuccessfully(httpResponse);
        orderNumberOptional.ifPresent(orderNumber -> registerOrderNumber(orderNumberAlias, orderNumber));
    }

    @Override
    public void confirmOrderPlaced(String orderNumberAlias, String prefix) {
        var httpResponse = ordersPlaced.get(orderNumberAlias);
        var response = apiClient.getOrderController().confirmOrderPlacedSuccessfully(httpResponse);

        assertNotNull(response.getOrderNumber(), "Order number should be not be null");
        assertFalse(response.getOrderNumber().isEmpty(), "Order number should be not be empty");
        assertTrue(response.getOrderNumber().startsWith(prefix), "Order number should start with prefix: " + prefix);
    }

    @Override
    public void viewOrderDetails(String orderNumberAlias) {
        var orderNumber = getOrderNumber(orderNumberAlias);
        var httpResponse = apiClient.getOrderController().viewOrder(orderNumber);
        registerOrderResponse(ordersViewed, orderNumberAlias, httpResponse);
    }

    @Override
    public void confirmOrderDetails(String orderNumberAlias, String productId, String quantity, String status) {
        // Fetch order details if not already viewed
        if (!ordersViewed.containsKey(orderNumberAlias)) {
            viewOrderDetails(orderNumberAlias);
        }

        var httpResponse = ordersViewed.get(orderNumberAlias);
        var response = apiClient.getOrderController().confirmOrderViewedSuccessfully(httpResponse);

        assertEquals(Long.parseLong(productId), response.getProductId());
        assertEquals(Long.parseLong(quantity), response.getQuantity());

        var unitPrice = response.getUnitPrice();
        assertNotNull(unitPrice, "Unit price should not be null");
        assertTrue(unitPrice.compareTo(BigDecimal.ZERO) > 0, "Unit price should be positive");

        var totalPrice = response.getTotalPrice();
        assertNotNull(totalPrice, "Total price should not be null");
        assertTrue(totalPrice.compareTo(BigDecimal.ZERO) > 0, "Total price should be positive");
    }

    @Override
    public void confirmOrderStatusIsCancelled(String orderNumberAlias) {
        // Fetch order details if not already viewed
        if (!ordersViewed.containsKey(orderNumberAlias)) {
            viewOrderDetails(orderNumberAlias);
        }

        var httpResponse = ordersViewed.get(orderNumberAlias);
        var response = apiClient.getOrderController().confirmOrderViewedSuccessfully(httpResponse);
        assertEquals("CANCELLED", response.getStatus(), "Order status should be CANCELLED");
    }

    @Override
    public void cancelOrder(String orderNumberAlias) {
        var orderNumber = getOrderNumber(orderNumberAlias);
        var httpResponse = apiClient.getOrderController().cancelOrder(orderNumber);
        registerOrderResponse(ordersCancelled, orderNumberAlias, httpResponse);
    }

    @Override
    public void confirmOrderCancelled(String orderNumberAlias) {
        var httpResponse = ordersCancelled.get(orderNumberAlias);
        apiClient.getOrderController().confirmOrderCancelledSuccessfully(httpResponse);
    }

    private static void registerOrderResponse(HashMap<String, HttpResponse<String>> map, String orderNumber, HttpResponse<String> httpResponse) {
        if(map.containsKey(orderNumber)) {
            throw new IllegalStateException("Response for order number " + orderNumber + " is already registered.");
        }

        map.put(orderNumber, httpResponse);
    }

    private void registerOrderNumber(String orderNumberAlias, String orderNumber) {
        if(orderNumbers.containsKey(orderNumberAlias)) {
            throw new IllegalStateException("Order number alias " + orderNumberAlias + " is already registered.");
        }

        orderNumbers.put(orderNumberAlias, orderNumber);
    }

    private String getOrderNumber(String orderNumberAlias) {
        if(!orderNumbers.containsKey(orderNumberAlias)) {
            throw new IllegalStateException("Order number alias " + orderNumberAlias + " is not registered.");
        }

        return orderNumbers.get(orderNumberAlias);
    }

    @Override
    public void close() {
        apiClient.close();
    }
}
