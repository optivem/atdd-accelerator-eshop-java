package com.optivem.atddaccelerator.template.systemtest.e2etests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class ApiE2eTest {

    private static final int DEFAULT_PORT = 8080;
    private static final String BASE_URL = "http://localhost:" + DEFAULT_PORT;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void placeOrder_shouldReturnOrderNumber() throws Exception {
        // Arrange
        PlaceOrderRequest requestDto = new PlaceOrderRequest();
        requestDto.setSku("1001");
        requestDto.setQuantity(5);
        
        String requestBody = objectMapper.writeValueAsString(requestDto);
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/shop/order"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Act
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(200, response.statusCode(), "Response status should be 200 OK");
        
        String responseBody = response.body();
        PlaceOrderResponse responseDto = objectMapper.readValue(responseBody, PlaceOrderResponse.class);
        
        // Verify response contains orderNumber
        assertNotNull(responseDto.getOrderNumber(), "Order number should not be null");
        assertTrue(responseDto.getOrderNumber().startsWith("ORD-"), "Order number should start with ORD-");
    }
    
    @Data
    static class PlaceOrderRequest {
        private String sku;
        private int quantity;
    }
    
    @Data
    static class PlaceOrderResponse {
        private String orderNumber;
    }
}