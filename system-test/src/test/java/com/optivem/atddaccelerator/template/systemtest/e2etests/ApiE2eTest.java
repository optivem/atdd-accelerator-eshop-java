package com.optivem.atddaccelerator.template.systemtest.e2etests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        String requestBody = """
            {
                "sku": "1001",
                "quantity": 5
            }
            """;
        
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
        JsonNode jsonResponse = objectMapper.readTree(responseBody);
        
        // Verify response contains orderNumber
        assertTrue(jsonResponse.has("orderNumber"), "Response should contain orderNumber field");
        
        String orderNumber = jsonResponse.get("orderNumber").asText();
        assertNotNull(orderNumber, "Order number should not be null");
        assertTrue(orderNumber.startsWith("ORD-"), "Order number should start with ORD-");
    }

    // @Test
    // void getTodos_shouldReturnTodoWithExpectedFormat() throws Exception {
    //     // DISCLAIMER: This is an example of a badly written test
    //     // which unfortunately simulates real-life software test projects.
    //     // This is the starting point for our ATDD Accelerator exercises.

    //     // Arrange
    //     HttpClient client = HttpClient.newHttpClient();
    //     HttpRequest request = HttpRequest.newBuilder()
    //             .uri(new URI("http://localhost:8080/api/todos/4"))
    //             .GET()
    //             .build();

    //     // Act
    //     HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    //     // Assert
    //     assertEquals(200, response.statusCode());
        
    //     String responseBody = response.body();
        
    //     // Verify JSON structure contains expected fields
    //     assertTrue(responseBody.contains("\"userId\""), "Response should contain userId field");
    //     assertTrue(responseBody.contains("\"id\""), "Response should contain id field");
    //     assertTrue(responseBody.contains("\"title\""), "Response should contain title field");
    //     assertTrue(responseBody.contains("\"completed\""), "Response should contain completed field");
        
    //     // Verify the specific todo has id 4
    //     assertTrue(responseBody.contains("\"id\":4") || responseBody.contains("\"id\": 4"), 
    //                "Response should contain id with value 4");
    // }
}