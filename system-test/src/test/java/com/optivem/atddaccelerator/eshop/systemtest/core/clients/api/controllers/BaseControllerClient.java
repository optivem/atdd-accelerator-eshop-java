package com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class BaseControllerClient {

    protected static final ObjectMapper objectMapper = new ObjectMapper();

    protected final HttpClient httpClient;
    private final String baseUrl;

    public BaseControllerClient(HttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
    }

    protected URI getBaseUri() {
        try {
            return new URI(baseUrl);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected URI getUri(String path) {
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

    protected String serializeRequest(Object request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to serialize request object", ex);
        }
    }

    protected HttpResponse<String> sendRequest(HttpRequest httpRequest) {
        try {
            return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to send HTTP request", ex);
        }
    }
}
