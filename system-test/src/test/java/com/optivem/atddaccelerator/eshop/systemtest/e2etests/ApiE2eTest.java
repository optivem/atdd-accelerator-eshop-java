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
    private ApiClient apiClient;

    @BeforeEach
    void setUp() {
        var baseUrl = TestConfiguration.getBaseUrl();
        apiClient = new ApiClient(baseUrl);
    }

    @AfterEach
    void tearDown() {
        if (apiClient != null) {
            apiClient.close();
        }
    }

    @Test
    void placeOrder_shouldReturnOrderNumber() throws Exception {
        // Arrange
        String productId = "10";
        String quantity = "5";

        // Act
        var httpResponse = apiClient.getOrderController().placeOrder(productId, quantity);

        // Assert
        var response = apiClient.getOrderController().confirmOrderPlacedSuccessfully(httpResponse);
        assertNotNull(response.getOrderNumber(), "Order number should not be null");
        assertTrue(response.getOrderNumber().startsWith("ORD-"), "Order number should start with ORD-");
    }

    @Test
    void getOrder_shouldReturnOrderDetails() throws Exception {
        // Arrange
        var productId = "11";
        var quantity = "3";

        var orderNumber = placeOrderAndGetOrderNumber(productId, quantity);
        
        // Act
        var httpResponse = apiClient.getOrderController().viewOrder(orderNumber);

        // Assert
        var response = apiClient.getOrderController().confirmOrderViewedSuccessfully(httpResponse);
        
        assertEquals(orderNumber, response.getOrderNumber(), "Order number should match");
        assertEquals(productId, response.getProductId(), "Product ID should match");
        assertEquals(quantity, response.getQuantity(), "Quantity should match");

        assertNotNull(response.getUnitPrice(), "Unit price should not be null");
        assertNotNull(response.getTotalPrice(), "Total price should not be null");
    }

    @Test
    void cancelOrder_shouldSetStatusToCancelled() {
        // Arrange
        var productId = "12";
        var quantity = "2";

        var orderNumber = placeOrderAndGetOrderNumber(productId, quantity);
        
        // Act
        var httpResponse = apiClient.getOrderController().cancelOrder(orderNumber);

        // Assert
        apiClient.getOrderController().confirmOrderCancelledSuccessfully(httpResponse);

        var orderDetails = getOrderDetails(orderNumber);
        assertEquals("CANCELLED", orderDetails.getStatus(), "Order status should be CANCELLED");
    }

    private String placeOrderAndGetOrderNumber(String productId, String quantity) {
        var httpResponse = apiClient.getOrderController().placeOrder(productId, quantity);
        var placeOrderResponse = apiClient.getOrderController().confirmOrderPlacedSuccessfully(httpResponse);
        return placeOrderResponse.getOrderNumber();
    }

    private GetOrderResponse getOrderDetails(String orderNumber) {
        var httpResponse = apiClient.getOrderController().viewOrder(orderNumber);
        return apiClient.getOrderController().confirmOrderViewedSuccessfully(httpResponse);
    }
}