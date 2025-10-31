package com.optivem.atddaccelerator.template.systemtest.smoketests;

import com.optivem.atddaccelerator.template.systemtest.TestConfiguration;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiSmokeTest {

    @Test
    void echo_shouldReturn200OK() throws Exception {
        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder()
                    .uri(new URI(TestConfiguration.getBaseUrl() + "/api/echo"))
                    .GET()
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
        }
    }
}