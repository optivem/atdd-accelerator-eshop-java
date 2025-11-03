package com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.controllers;

import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EchoControllerClient extends BaseControllerClient {

    public EchoControllerClient(HttpClient client, String baseUrl) {
        super(client, baseUrl);
    }

    public HttpResponse<String> echo() {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(new URI(TestConfiguration.getBaseUrl() + "/api/echo"))
                    .GET()
                    .build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
