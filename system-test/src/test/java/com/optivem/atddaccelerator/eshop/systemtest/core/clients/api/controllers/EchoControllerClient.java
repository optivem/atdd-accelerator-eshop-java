package com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.controllers;

import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EchoControllerClient extends BaseControllerClient {

    public EchoControllerClient(HttpClient client, String baseUrl) {
        super(client, baseUrl);
    }

    public HttpResponse<String> echo() {
        return get("api/echo");
    }

    public void confirmEchoSuccessful(HttpResponse<String> httpResponse) {
        assertOk(httpResponse);
    }

}
