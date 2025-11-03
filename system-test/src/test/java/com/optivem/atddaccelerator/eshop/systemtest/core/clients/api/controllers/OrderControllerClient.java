package com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.controllers;

import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.dtos.GetOrderResponse;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.dtos.PlaceOrderRequest;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.dtos.PlaceOrderResponse;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderControllerClient extends BaseControllerClient {
    public OrderControllerClient(HttpClient client, String baseUrl) {
        super(client, baseUrl);
    }

    public PlaceOrderResponse placeOrderSuccessfully(long productId, int quantity) throws Exception {
        var request = new PlaceOrderRequest();
        request.setProductId(productId);
        request.setQuantity(quantity);

        var requestBody = objectMapper.writeValueAsString(request);

        var uri = getUri("api/orders");

        var httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode(), "Response status should be 200 OK");

        var responseBody = httpResponse.body();
        return objectMapper.readValue(responseBody, PlaceOrderResponse.class);
    }

    public GetOrderResponse getOrderSuccessfully(String orderNumber) throws Exception {
        var uri = getUri("api/orders/" + orderNumber);

        var httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode(), "Response status should be 200 OK");

        var responseBody = httpResponse.body();
        return objectMapper.readValue(responseBody, GetOrderResponse.class);
    }

    public void cancelOrderSuccessfully(String orderNumber) throws Exception {
        var uri = getUri("api/orders/" + orderNumber + "/cancel");

        var httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, httpResponse.statusCode(), "Response status should be 200 OK");
    }

}
