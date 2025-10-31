package com.optivem.atddaccelerator.template.systemtest.smoketests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiSmokeTest {

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    @Test
    void echo_shouldReturn200OK() throws Exception {
        var request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/echo"))
                .GET()
                .build();

        var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }
}