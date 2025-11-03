package com.optivem.atddaccelerator.eshop.systemtest.smoketests;

import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.ApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiSmokeTest {

    private ApiClient apiClient;

    @BeforeEach
    void setUp() {
        this.apiClient = new ApiClient(TestConfiguration.getBaseUrl());
    }

    @Test
    void echo_shouldReturn200OK() throws Exception {
        var httpResponse = apiClient.getEchoController().echo();
        apiClient.getEchoController().confirmEchoSuccessful(httpResponse);
    }
}