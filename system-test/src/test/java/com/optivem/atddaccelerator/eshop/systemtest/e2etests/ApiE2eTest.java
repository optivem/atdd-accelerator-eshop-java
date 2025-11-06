package com.optivem.atddaccelerator.eshop.systemtest.e2etests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;
import lombok.Data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

class ApiE2eTest {

    private static final String BASE_URL = TestConfiguration.getBaseUrl();
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
        requestDto.setProductId("10");
        requestDto.setQuantity("5");

        var requestBody = objectMapper.writeValueAsString(requestDto);
        
        var request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/orders"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Act
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(201, response.statusCode(), "Response status should be 201 CREATED");

        var responseBody = response.body();
        var responseDto = objectMapper.readValue(responseBody, PlaceOrderResponse.class);
        
        // Verify response contains orderNumber
        assertNotNull(responseDto.getOrderNumber(), "Order number should not be null");
        assertTrue(responseDto.getOrderNumber().startsWith("ORD-"), "Order number should start with ORD-");
    }

    @ParameterizedTest
    @MethodSource("provideOrderTestCases")
    void getOrder_shouldReturnOrderDetails(long productId, int quantity) throws Exception {
        // Arrange - First place an order
        var placeOrderRequest = new PlaceOrderRequest();
        placeOrderRequest.setProductId(String.valueOf(productId));
        placeOrderRequest.setQuantity(String.valueOf(quantity));

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
        assertEquals(productId, getOrderResponse.getProductId(), "Product ID should be " + productId);
        assertEquals(quantity, getOrderResponse.getQuantity(), "Quantity should be " + quantity);

        // Price will come from DummyJSON API for product
        assertNotNull(getOrderResponse.getUnitPrice(), "Unit price should not be null");
        assertNotNull(getOrderResponse.getTotalPrice(), "Total price should not be null");
    }

    private static Stream<Arguments> provideOrderTestCases() {
        return Stream.of(
            Arguments.of(11L, 3),   // Product 11 with standard quantity
            Arguments.of(12L, 5),   // Product 12 with medium quantity
            Arguments.of(13L, 1),   // Product 13 with minimum quantity
            Arguments.of(14L, 10)   // Product 14 with large quantity
        );
    }

    @Test
    void cancelOrder_shouldSetStatusToCancelled() throws Exception {
        // Arrange - First place an order
        var placeOrderRequest = new PlaceOrderRequest();
        placeOrderRequest.setProductId("12");
        placeOrderRequest.setQuantity("2");

        var requestBody = objectMapper.writeValueAsString(placeOrderRequest);
        
        var postRequest = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/orders"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        var postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        var placeOrderResponse = objectMapper.readValue(postResponse.body(), PlaceOrderResponse.class);
        var orderNumber = placeOrderResponse.getOrderNumber();
        
        // Act - Cancel the order
        var cancelRequest = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/orders/" + orderNumber + "/cancel"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        var cancelResponse = httpClient.send(cancelRequest, HttpResponse.BodyHandlers.ofString());

        // Assert - Verify cancel response
        assertEquals(204, cancelResponse.statusCode(), "Response status should be 204 No Content");

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


    @Test
    void shouldRejectOrderWithNegativeQuantity() throws Exception {
        // Arrange
        var requestDto = new PlaceOrderRequest();
        requestDto.setProductId("10");
        requestDto.setQuantity("-5");

        var requestBody = objectMapper.writeValueAsString(requestDto);

        var request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/orders"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Act
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(422, response.statusCode(), "Response status should be 422 Unprocessable Entity");

        var responseBody = response.body();
        assertTrue(responseBody.contains("Quantity must be positive"),
                "Error message should be 'Quantity must be positive'. Actual: " + responseBody);
    }

    private static Stream<Arguments> provideInvalidQuantityValues() {
        return Stream.of(
                Arguments.of("3.5"),    // Decimal value
                Arguments.of("lala")    // String value
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidProductIdValues")
    void shouldRejectOrderWithNonIntegerProductId(String productIdValue) throws Exception {
        // Arrange
        var requestDto = new PlaceOrderRequest();
        requestDto.setProductId(productIdValue);
        requestDto.setQuantity("5");

        var requestBody = objectMapper.writeValueAsString(requestDto);

        var request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/orders"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Act
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(400, response.statusCode(), "Response status should be 400 Bad Request for productId: " + productIdValue);

        var responseBody = response.body();
        assertTrue(responseBody.contains("Product ID must be an integer"),
                "Error message should be 'Product ID must be an integer'. Actual: " + responseBody);
    }

    private static Stream<Arguments> provideInvalidProductIdValues() {
        return Stream.of(
                Arguments.of("10.5"),   // Decimal value
                Arguments.of("xyz")     // String value
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidQuantityValues")
    void shouldRejectOrderWithNonIntegerQuantity(String quantityValue) throws Exception {
        // Arrange
        var requestDto = new PlaceOrderRequest();
        requestDto.setProductId("10");
        requestDto.setQuantity(quantityValue);

        var requestBody = objectMapper.writeValueAsString(requestDto);

        var request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/api/orders"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Act
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(400, response.statusCode(), "Response status should be 400 Bad Request for quantity: " + quantityValue);

        var responseBody = response.body();
        assertTrue(responseBody.contains("Quantity must be an integer"),
                "Error message should be 'Quantity must be an integer'. Actual: " + responseBody);
    }



    @Data
    static class PlaceOrderRequest {
        private String productId;
        private String quantity;
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
        private String status;
    }
}