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

    public HttpResponse<String> placeOrder(String productId, String quantity) {
        var request = new PlaceOrderRequest();
        request.setProductId(productId);
        request.setQuantity(quantity);

        var requestBody = serializeRequest(request);

        var uri = getUri("api/orders");

        var httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return sendRequest(httpRequest);
    }

    public PlaceOrderResponse confirmOrderPlacedSuccessfully(HttpResponse<String> httpResponse) {
        assertEquals(HttpStatus.CREATED.value(), httpResponse.statusCode());
        return readBody(httpResponse, PlaceOrderResponse.class);
    }

    public HttpResponse<String> viewOrder(String orderNumber) {
        var uri = getUri("api/orders/" + orderNumber);

        var httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        return sendRequest(httpRequest);
    }

    public GetOrderResponse confirmOrderViewedSuccessfully(HttpResponse<String> httpResponse) {
        assertEquals(HttpStatus.OK.value(), httpResponse.statusCode());
        return readBody(httpResponse, GetOrderResponse.class);
    }

    public HttpResponse<String> cancelOrder(String orderNumber) {
        var uri = getUri("api/orders/" + orderNumber + "/cancel");

        var httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        return sendRequest(httpRequest);
    }

    public void confirmOrderCancelledSuccessfully(HttpResponse<String> httpResponse) {
        assertEquals(HttpStatus.NO_CONTENT.value(), httpResponse.statusCode());
    }

}
