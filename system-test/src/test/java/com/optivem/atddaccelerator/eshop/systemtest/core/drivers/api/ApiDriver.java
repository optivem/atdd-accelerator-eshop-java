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
    public void placeOrder(String order, String productId, String quantity) {
        var httpResponse = apiClient.getOrderController().placeOrder(productId, quantity);
        registerOrderResponse(ordersPlaced, order, httpResponse);

        var orderNumberOptional = apiClient.getOrderController().getOrderNumberIfOrderPlacedSuccessfully(httpResponse);
        orderNumberOptional.ifPresent(orderNumber -> registerOrderNumber(order, orderNumber));
    }

    @Override
    public void confirmOrderPlaced(String order, String prefix) {
        var httpResponse = ordersPlaced.get(order);
        var response = apiClient.getOrderController().confirmOrderPlacedSuccessfully(httpResponse);

        assertNotNull(response.getOrderNumber(), "Order number should be not be null");
        assertFalse(response.getOrderNumber().isEmpty(), "Order number should be not be empty");
        assertTrue(response.getOrderNumber().startsWith(prefix), "Order number should start with prefix: " + prefix);
    }

    @Override
    public void viewOrderDetails(String order) {
        var orderNumber = getOrderNumber(order);
        var httpResponse = apiClient.getOrderController().viewOrder(orderNumber);
        registerOrderResponse(ordersViewed, order, httpResponse);
    }

    @Override
    public void confirmOrderDetails(String order, String productId, String quantity, String status) {
        // Fetch order details if not already viewed
        if (!ordersViewed.containsKey(order)) {
            viewOrderDetails(order);
        }

        var httpResponse = ordersViewed.get(order);
        var response = apiClient.getOrderController().confirmOrderViewedSuccessfully(httpResponse);

        assertEquals(Long.parseLong(productId), response.getProductId());
        assertEquals(Long.parseLong(quantity), response.getQuantity());

        var unitPrice = response.getUnitPrice();
        assertNotNull(unitPrice, "Unit price should not be null");
        assertTrue(unitPrice.compareTo(BigDecimal.ZERO) > 0, "Unit price should be positive");

        var totalPrice = response.getTotalPrice();
        assertNotNull(totalPrice, "Total price should not be null");
        assertTrue(totalPrice.compareTo(BigDecimal.ZERO) > 0, "Total price should be positive");

        assertEquals(status, response.getStatus(), "Order status should be: " + status);
    }

    @Override
    public void confirmOrderStatusIsCancelled(String order) {
        // Fetch order details if not already viewed
        if (!ordersViewed.containsKey(order)) {
            viewOrderDetails(order);
        }

        var httpResponse = ordersViewed.get(order);
        var response = apiClient.getOrderController().confirmOrderViewedSuccessfully(httpResponse);
        assertEquals("CANCELLED", response.getStatus(), "Order status should be CANCELLED");
    }

    @Override
    public void cancelOrder(String order) {
        var orderNumber = getOrderNumber(order);
        var httpResponse = apiClient.getOrderController().cancelOrder(orderNumber);
        registerOrderResponse(ordersCancelled, order, httpResponse);
    }

    @Override
    public void confirmOrderCancelled(String order) {
        var httpResponse = ordersCancelled.get(order);
        apiClient.getOrderController().confirmOrderCancelledSuccessfully(httpResponse);
    }


    @Override
    public void confirmOrderPlacementFailed(String order, String errorMessage) {
        var httpResponse = ordersPlaced.get(order);
        assertNotNull(httpResponse, "Order placement response should exist");

        // Check that the HTTP response indicates unprocessable entity (422)
        assertEquals(422, httpResponse.statusCode(),
                   "Expected 422 Unprocessable Entity status code, but got: " + httpResponse.statusCode());

        // Check that the error message contains the expected text
        var responseBody = httpResponse.body();
        assertTrue(responseBody.contains(errorMessage),
                   "Expected error message to contain: " + errorMessage + ", but got: " + responseBody);
    }

    private static void registerOrderResponse(HashMap<String, HttpResponse<String>> map, String orderNumber, HttpResponse<String> httpResponse) {
        if(map.containsKey(orderNumber)) {
            throw new IllegalStateException("Response for order number " + orderNumber + " is already registered.");
        }

        map.put(orderNumber, httpResponse);
    }

    private void registerOrderNumber(String order, String orderNumber) {
        if(orderNumbers.containsKey(order)) {
            throw new IllegalStateException("Order number alias " + order + " is already registered.");
        }

        orderNumbers.put(order, orderNumber);
    }

    private String getOrderNumber(String order) {
        if(!orderNumbers.containsKey(order)) {
            throw new IllegalStateException("Order number alias " + order + " is not registered.");
        }

        return orderNumbers.get(order);
    }

    @Override
    public void close() {
        apiClient.close();
    }
}
