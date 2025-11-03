package com.optivem.atddaccelerator.eshop.systemtest.core.drivers.api;

import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.ApiClient;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.Driver;

import java.net.http.HttpResponse;
import java.util.HashMap;

public class ApiDriver implements Driver {

    private final ApiClient apiClient;

    private final HashMap<String, HttpResponse<String>> ordersPlaced;
    private final HashMap<String, HttpResponse<String>> ordersViewed;
    private final HashMap<String, HttpResponse<String>> ordersCancelled;

    public ApiDriver(String baseUrl) {
        this.apiClient = new ApiClient(baseUrl);
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
    public void placeOrder(String orderNumber, String productId, String quantity) {
        var httpResponse = apiClient.getOrderController().placeOrder(productId, quantity);
        registerResponse(ordersPlaced, orderNumber, httpResponse);
    }

    @Override
    public void confirmOrderCreated(String orderNumber) {
        var httpResponse = ordersPlaced.get(orderNumber);
        apiClient.getOrderController().confirmOrderPlacedSuccessfully(httpResponse);
    }

    @Override
    public void viewOrderDetails(String orderNumber) {
        var httpResponse = apiClient.getOrderController().viewOrder(orderNumber);
        registerResponse(ordersViewed, orderNumber, httpResponse);
    }

    @Override
    public void confirmOrderDetailsExist(String orderNumber) {
        var httpResponse = ordersViewed.get(orderNumber);
        apiClient.getOrderController().confirmOrderViewedSuccessfully(httpResponse);
    }

    @Override
    public void cancelOrder(String orderNumber) {
        var httpResponse = apiClient.getOrderController().cancelOrder(orderNumber);
        registerResponse(ordersCancelled, orderNumber, httpResponse);
    }

    @Override
    public void confirmOrderCancelled(String orderNumber) {
        var httpResponse = ordersCancelled.get(orderNumber);
        apiClient.getOrderController().confirmOrderCancelledSuccessfully(httpResponse);
    }

    private static void registerResponse(HashMap<String, HttpResponse<String>> map, String orderNumber, HttpResponse<String> httpResponse) {
        if(map.containsKey(orderNumber)) {
            throw new IllegalStateException("Response for order number " + orderNumber + " is already registered.");
        }

        map.put(orderNumber, httpResponse);
    }

    @Override
    public void close() {
        apiClient.close();
    }
}
