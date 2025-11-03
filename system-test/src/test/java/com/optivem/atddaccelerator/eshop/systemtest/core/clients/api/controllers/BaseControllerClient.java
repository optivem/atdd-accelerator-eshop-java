package com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;

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
}
