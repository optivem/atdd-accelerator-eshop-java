package com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.controllers;

import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.dtos.GetOrderResponse;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.dtos.PlaceOrderRequest;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.dtos.PlaceOrderResponse;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderControllerClient extends BaseControllerClient {
    public OrderControllerClient(HttpClient client, String baseUrl) {
        super(client, baseUrl);
    }

    public HttpResponse<String> placeOrder(long productId, int quantity) {
        var request = new PlaceOrderRequest();
        request.setProductId(productId);
        request.setQuantity(quantity);

        return post("api/orders", request);
    }

    public PlaceOrderResponse confirmOrderPlacedSuccessfully(HttpResponse<String> httpResponse) {
        assertCreated(httpResponse);
        return readBody(httpResponse, PlaceOrderResponse.class);
    }

    public HttpResponse<String> viewOrder(String orderNumber) {
        var endpoint = "api/orders/" + orderNumber;
        return get(endpoint);
    }

    public GetOrderResponse confirmOrderViewedSuccessfully(HttpResponse<String> httpResponse) {
        assertOk(httpResponse);
        return readBody(httpResponse, GetOrderResponse.class);
    }

    public HttpResponse<String> cancelOrder(String orderNumber) {
        var endpoint = "api/orders/" + orderNumber + "/cancel";
        return post(endpoint);
    }

    public void confirmOrderCancelledSuccessfully(HttpResponse<String> httpResponse) {
        assertNoContent(httpResponse);
    }
}
