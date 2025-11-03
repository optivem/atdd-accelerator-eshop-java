package com.optivem.atddaccelerator.eshop.systemtest.e2etests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.ApiClient;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.dtos.GetOrderResponse;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.dtos.PlaceOrderRequest;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.dtos.PlaceOrderResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class ApiE2eTest {

    private static final String BASE_URL = TestConfiguration.getBaseUrl();

    protected static final ObjectMapper objectMapper = new ObjectMapper();

    private HttpClient httpClient;

    private ApiClient apiClient;

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
        apiClient = new ApiClient(BASE_URL);
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
        var productId = 10;
        var quantity = 5;

        // Act
        var response = apiClient.getOrderController().placeOrderSuccessfully(productId, quantity);

        // Assert
        assertNotNull(response.getOrderNumber(), "Order number should not be null");
        assertTrue(response.getOrderNumber().startsWith("ORD-"), "Order number should start with ORD-");
    }

    @Test
    void getOrder_shouldReturnOrderDetails() throws Exception {
        // Arrange - First place an order
        var productId = 11;
        var quantity = 3;

        var placeOrderResponse = apiClient.getOrderController().placeOrderSuccessfully(productId, quantity);

        var orderNumber = placeOrderResponse.getOrderNumber();
        
        // Act - Get the order details
        var getOrderResponse = apiClient.getOrderController().getOrderSuccessfully(orderNumber);
        
        assertEquals(orderNumber, getOrderResponse.getOrderNumber(), "Order number should match");
        assertEquals(11L, getOrderResponse.getProductId(), "Product ID should be 11");
        assertEquals(3, getOrderResponse.getQuantity(), "Quantity should be 3");
        
        // Price will come from DummyJSON API for product 11
        assertNotNull(getOrderResponse.getUnitPrice(), "Unit price should not be null");
        assertNotNull(getOrderResponse.getTotalPrice(), "Total price should not be null");
    }

    @Test
    void cancelOrder_shouldSetStatusToCancelled() throws Exception {
        // Arrange - First place an order
        var productId = 12;
        var quantity = 2;

        var placeOrderResponse = apiClient.getOrderController().placeOrderSuccessfully(productId, quantity);
        var orderNumber = placeOrderResponse.getOrderNumber();
        
        // Act - Cancel the order
        var deleteRequest = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/orders/" + orderNumber))
                .DELETE()
                .build();

        var deleteResponse = httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        // Assert - Verify cancel response
        assertEquals(204, deleteResponse.statusCode(), "Response status should be 204 No Content");
        
        // Verify order status is CANCELLED
        var getRequest = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/orders/" + orderNumber))
                .GET()
                .build();

        var getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getResponse.statusCode(), "Response status should be 200 OK");
        
        var getOrderResponse = objectMapper.readValue(getResponse.body(), GetOrderResponse.class);
        assertEquals("CANCELLED", getOrderResponse.getStatus(), "Order status should be CANCELLED");
    }
    

    

    

}