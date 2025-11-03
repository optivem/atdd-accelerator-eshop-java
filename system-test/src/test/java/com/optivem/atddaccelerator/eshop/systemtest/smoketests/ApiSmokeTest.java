package com.optivem.atddaccelerator.eshop.systemtest.smoketests;

import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.ApiClient;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.api.ApiDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiSmokeTest {

    private ApiDriver apiDriver;

    @BeforeEach
    void setUp() {
        var baseUrl = TestConfiguration.getBaseUrl();
        this.apiDriver = new ApiDriver(baseUrl);
    }

    @AfterEach
    void tearDown() {
        if (apiDriver != null) {
            apiDriver.close();
        }
    }

    @Test
    void echo_shouldReturn200OK() {
        apiDriver.goToShop();
    }
}