package com.optivem.atddaccelerator.template.monolith.core.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ErpGateway {

    @Value("${erp.url}")
    private String erpUrl;

    public BigDecimal getUnitPrice(long productId) {
        try {
            System.out.println("Going to contact: " + erpUrl);

            var url = erpUrl + "/products/" + productId;
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var mapper = new ObjectMapper();
            var node = mapper.readTree(response.body());
            return BigDecimal.valueOf(node.get("price").asDouble()).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch price", e);
        }
    }
}
