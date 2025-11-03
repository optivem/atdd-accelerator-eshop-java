package com.optivem.atddaccelerator.eshop.systemtest.core.clients.api;

import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.controllers.EchoControllerClient;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.controllers.OrderControllerClient;

import java.net.http.HttpClient;

public class ApiClient implements AutoCloseable {

    private final HttpClient client;

    private final EchoControllerClient echoControllerClient;
    private final OrderControllerClient orderControllerClient;

    public ApiClient(String baseUrl) {
        this.client = HttpClient.newHttpClient();
        this.echoControllerClient = new EchoControllerClient(client, baseUrl);
        this.orderControllerClient = new OrderControllerClient(client, baseUrl);
    }

    public EchoControllerClient getEchoController() {
        return echoControllerClient;
    }

    public OrderControllerClient getOrderController() {
        return orderControllerClient;
    }

    @Override
    public void close() throws Exception {
        client.close();
    }
}
