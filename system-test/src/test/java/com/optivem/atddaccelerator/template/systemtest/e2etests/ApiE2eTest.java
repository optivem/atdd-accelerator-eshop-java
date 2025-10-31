package com.optivem.atddaccelerator.template.systemtest.e2etests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optivem.atddaccelerator.template.systemtest.TestConfiguration;
import lombok.Data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class ApiE2eTest {

    private static final String BASE_URL = "http://localhost:" + TestConfiguration.getServerPort();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        if (httpClient != null) {
            httpClient.close();
        }
    }

    @Test
    void placeOrder_shouldReturnOrderNumber() throws Exception {
        // Arrange
        var requestDto = new PlaceOrderRequest();
        requestDto.setProductId(10);
        requestDto.setQuantity(5);
        
        var requestBody = objectMapper.writeValueAsString(requestDto);
        
        var request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/orders"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Act
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(200, response.statusCode(), "Response status should be 200 OK");
        
        var responseBody = response.body();
        var responseDto = objectMapper.readValue(responseBody, PlaceOrderResponse.class);
        
        // Verify response contains orderNumber
        assertNotNull(responseDto.getOrderNumber(), "Order number should not be null");
        assertTrue(responseDto.getOrderNumber().startsWith("ORD-"), "Order number should start with ORD-");
    }

    @Test
    void getOrder_shouldReturnOrderDetails() throws Exception {
        // Arrange - First place an order
        var placeOrderRequest = new PlaceOrderRequest();
        placeOrderRequest.setProductId(11);
        placeOrderRequest.setQuantity(3);
        
        var requestBody = objectMapper.writeValueAsString(placeOrderRequest);
        
        var postRequest = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/orders"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        var postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        var placeOrderResponse = objectMapper.readValue(postResponse.body(), PlaceOrderResponse.class);
        var orderNumber = placeOrderResponse.getOrderNumber();
        
        // Act - Get the order details
        var getRequest = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/orders/" + orderNumber))
                .GET()
                .build();

        var getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(200, getResponse.statusCode(), "Response status should be 200 OK");
        
        var getOrderResponse = objectMapper.readValue(getResponse.body(), GetOrderResponse.class);
        
        assertEquals(orderNumber, getOrderResponse.getOrderNumber(), "Order number should match");
        assertEquals(11L, getOrderResponse.getProductId(), "Product ID should be 11");
        assertEquals(3, getOrderResponse.getQuantity(), "Quantity should be 3");
        
        // Price will come from DummyJSON API for product 11
        assertNotNull(getOrderResponse.getUnitPrice(), "Unit price should not be null");
        assertNotNull(getOrderResponse.getTotalPrice(), "Total price should not be null");
    }
    
    @Data
    static class PlaceOrderRequest {
        private long productId;
        private int quantity;
    }
    
    @Data
    static class PlaceOrderResponse {
        private String orderNumber;
        private BigDecimal totalPrice;
    }
    
    @Data
    static class GetOrderResponse {
        private String orderNumber;
        private long productId;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }
}