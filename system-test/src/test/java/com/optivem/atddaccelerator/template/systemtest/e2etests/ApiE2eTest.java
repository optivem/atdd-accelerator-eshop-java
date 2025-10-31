package com.optivem.atddaccelerator.template.systemtest.e2etests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optivem.atddaccelerator.template.systemtest.TestConfiguration;
import lombok.Data;

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

    @Disabled("Disabled until getOrder API is implemented")
    @Test
    void getOrder_shouldReturnOrderDetails() throws Exception {
        // Arrange - First place an order
        PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest();
        placeOrderRequest.setSku("2001");
        placeOrderRequest.setQuantity(3);
        
        String requestBody = objectMapper.writeValueAsString(placeOrderRequest);
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/shop/order"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        PlaceOrderResponse placeOrderResponse = objectMapper.readValue(postResponse.body(), PlaceOrderResponse.class);
        String orderNumber = placeOrderResponse.getOrderNumber();
        
        // Act - Get the order details
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/shop/order/" + orderNumber))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(200, getResponse.statusCode(), "Response status should be 200 OK");
        
        GetOrderResponse getOrderResponse = objectMapper.readValue(getResponse.body(), GetOrderResponse.class);
        
        assertEquals(orderNumber, getOrderResponse.getOrderNumber(), "Order number should match");
        assertEquals(2001L, getOrderResponse.getProductId(), "Product ID should be 2001");
        assertEquals(3, getOrderResponse.getQuantity(), "Quantity should be 3");
        
        // Expected price: 2001 / 1000 * 3 = 6.003
        BigDecimal expectedPrice = new BigDecimal("6.00");
        assertEquals(0, expectedPrice.compareTo(getOrderResponse.getTotalPrice()), 
                "Total price should be 6.00, but was: " + getOrderResponse.getTotalPrice());
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
    
    @Data
    static class GetOrderResponse {
        private String orderNumber;
        private long productId;
        private int quantity;
        private BigDecimal totalPrice;
    }
}