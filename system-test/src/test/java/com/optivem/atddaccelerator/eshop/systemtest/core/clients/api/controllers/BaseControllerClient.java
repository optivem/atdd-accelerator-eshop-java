package com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class BaseControllerClient {

    protected static final ObjectMapper objectMapper = new ObjectMapper();

    protected final HttpClient httpClient;
    private final String baseUrl;

    public BaseControllerClient(HttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
    }

    protected HttpResponse<String> get(String endpoint) {
        var uri = getUri(endpoint);
        var request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        return sendRequest(request);
    }

    protected HttpResponse<String> post(String endpoint, Object requestBody) {
        var uri = getUri(endpoint);
        var jsonBody = serializeRequest(requestBody);

        var request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return sendRequest(request);
    }

    protected HttpResponse<String> post(String endpoint) {
        var uri = getUri(endpoint);

        var request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        return sendRequest(request);
    }

    protected void assertOk(HttpResponse<String> httpResponse) {
        assertStatus(httpResponse, HttpStatus.OK);
    }

    protected void assertCreated(HttpResponse<String> httpResponse) {
        assertStatus(httpResponse, HttpStatus.CREATED);
    }

    protected void assertNoContent(HttpResponse<String> httpResponse) {
        assertStatus(httpResponse, HttpStatus.NO_CONTENT);
    }

    private void assertStatus(HttpResponse<String> httpResponse, HttpStatus expectedStatus) {
        assertEquals(expectedStatus.value(), httpResponse.statusCode());

    }

    private URI getUri(String path) {
        try {
            return new URI(baseUrl + "/" + path);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected <T> T readBody(HttpResponse<String> httpResponse, Class<T> responseType) {
        try {
            var responseBody = httpResponse.body();
            return objectMapper.readValue(responseBody, responseType);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to deserialize response to " + responseType.getSimpleName(), ex);
        }
    }

    private String serializeRequest(Object request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to serialize request object", ex);
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest httpRequest) {
        try {
            return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to send HTTP request", ex);
        }
    }
}
