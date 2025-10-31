package com.optivem.atddaccelerator.template.monolith.core.services.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optivem.atddaccelerator.template.monolith.core.dtos.external.ProductPriceResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
public class ErpGateway {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    @Value("${erp.url}")
    private String erpUrl;

    public BigDecimal getUnitPrice(long productId) {
        try (var httpClient = HttpClient.newHttpClient()) {
            var url = erpUrl + "/products/" + productId;
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            var productPriceResponse = OBJECT_MAPPER.readValue(response.body(), ProductPriceResponse.class);

            return productPriceResponse.getPrice();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch price for product: " + productId, e);
        }
    }
}
