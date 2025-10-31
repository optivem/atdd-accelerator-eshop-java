package com.optivem.atddaccelerator.template.monolith.common;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PriceCalculator {
    
    @Value("${erp.url}")
    private String erpUrl;
    
    public double calculatePrice(long productId, int quantity) {
        var price = getPrice(productId);
        return price * quantity;
    }

    private double getPrice(long productId) {
        try {
            System.out.println("Going to contact: " + erpUrl);

            var url = erpUrl + "/products/" + productId;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.body());
            return node.get("price").asDouble();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch price", e);
        }
    }
}
