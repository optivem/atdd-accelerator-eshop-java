package com.optivem.atddaccelerator.template.monolith.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    @Value("${erp.url}")
    private String erpUrl;
    
    public BigDecimal getUnitPrice(long productId) {
        try {
            System.out.println("Going to contact: " + erpUrl);

            var url = erpUrl + "/products/" + productId;
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            var node = OBJECT_MAPPER.readTree(response.body());
            return BigDecimal.valueOf(node.get("price").asDouble()).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch price", e);
        }
    }
    
    public BigDecimal calculateTotalPrice(BigDecimal unitPrice, int quantity) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
