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
    public void confirmOrderCreated(String orderNumberAlias) {
        var httpResponse = ordersPlaced.get(orderNumberAlias);
        var response = apiClient.getOrderController().confirmOrderPlacedSuccessfully(httpResponse);
    }

    @Override
    public void viewOrderDetails(String orderNumberAlias) {
        var orderNumber = getOrderNumber(orderNumberAlias);
        var httpResponse = apiClient.getOrderController().viewOrder(orderNumber);
        registerOrderResponse(ordersViewed, orderNumberAlias, httpResponse);
    }

    @Override
    public void confirmOrderDetailsExist(String orderNumberAlias) {
        var httpResponse = ordersViewed.get(orderNumberAlias);
        apiClient.getOrderController().confirmOrderViewedSuccessfully(httpResponse);
    }

    @Override
    public void confirmOrderDetailsHaveOrderNumber(String orderNumberAlias) {
        // TODO: VJ: DELETE method fro base

    }

    @Override
    public void confirmOrderDetailsHaveProductId(String orderNumberAlias, String productId) {
        var httpResponse = ordersViewed.get(orderNumberAlias);
        var response = apiClient.getOrderController().confirmOrderViewedSuccessfully(httpResponse);
        assertEquals(productId, response.getProductId());
    }

    @Override
    public void confirmOrderDetailsHaveQuantity(String orderNumberAlias, String quantity) {
        var httpResponse = ordersViewed.get(orderNumberAlias);
        var response = apiClient.getOrderController().confirmOrderViewedSuccessfully(httpResponse);
        assertEquals(quantity, response.getQuantity());
    }

    @Override
    public void confirmOrderDetailsHavePositiveUnitPrice(String orderNumberAlias) {
        var httpResponse = ordersViewed.get(orderNumberAlias);
        var response = apiClient.getOrderController().confirmOrderViewedSuccessfully(httpResponse);
        assertNotNull(response.getUnitPrice(), "Unit price should not be null");
        var decimalUnitPrice = new BigDecimal(response.getUnitPrice());
        assertTrue(decimalUnitPrice.compareTo(BigDecimal.ZERO) > 0, "Unit price should be positive");
    }

    @Override
    public void confirmOrderDetailsHavePositiveTotalPrice(String orderNumberAlias) {
        var httpResponse = ordersViewed.get(orderNumberAlias);
        var response = apiClient.getOrderController().confirmOrderViewedSuccessfully(httpResponse);
        assertNotNull(response.getTotalPrice(), "Total price should not be null");
        var decimalTotalPrice = new BigDecimal(response.getTotalPrice());
        assertTrue(decimalTotalPrice.compareTo(BigDecimal.ZERO) > 0, "Total price should be positive");
    }

    @Override
    public void confirmOrderStatusIsCancelled(String orderNumberAlias) {
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

    @Override
    public void confirmOrderNumberGenerated(String orderNumberAlias) {
        var httpResponse = ordersPlaced.get(orderNumberAlias);
        var response = apiClient.getOrderController().confirmOrderPlacedSuccessfully(httpResponse);
        assertNotNull(response.getOrderNumber(), "Order number should be not be null");
        assertFalse(response.getOrderNumber().isEmpty(), "Order number should be not be empty");
    }

    @Override
    public void confirmOrderNumberStartsWith(String orderNumberAlias, String prefix) {
        var httpResponse = ordersPlaced.get(orderNumberAlias);
        var response = apiClient.getOrderController().confirmOrderPlacedSuccessfully(httpResponse);
        assertTrue(response.getOrderNumber().startsWith(prefix), "Order number should start with prefix: " + prefix);
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
