package com.optivem.atddaccelerator.template.monolith.api.controller;

import com.optivem.atddaccelerator.template.monolith.common.Order;
import com.optivem.atddaccelerator.template.monolith.common.OrderStorage;
import com.optivem.atddaccelerator.template.monolith.common.PriceCalculator;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class ShopApiController {

    private final PriceCalculator priceCalculator;
    
    public ShopApiController(PriceCalculator priceCalculator) {
        this.priceCalculator = priceCalculator;
    }

    @PostMapping("/api/shop/order")
    public ResponseEntity<PlaceOrderResponse> placeOrder(@RequestBody PlaceOrderRequest request) {
        var orderNumber = OrderStorage.nextOrderNumber();
        var sku = request.getSku();
        var quantity = request.getQuantity();
        var productId = Long.parseLong(sku);
        BigDecimal unitPrice = priceCalculator.getUnitPrice(productId);
        BigDecimal totalPrice = priceCalculator.calculateTotalPrice(unitPrice, quantity);
        var order = new Order(orderNumber, productId, quantity, unitPrice, totalPrice);

        OrderStorage.saveOrder(order);

        var response = new PlaceOrderResponse();
        response.setOrderNumber(orderNumber);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/shop/order/{orderNumber}")
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable String orderNumber) {
        var order = OrderStorage.getOrder(orderNumber);

        var response = new GetOrderResponse();
        response.setOrderNumber(orderNumber);
        response.setProductId(order.getProductId());
        response.setQuantity(order.getQuantity());
        response.setUnitPrice(order.getUnitPrice());
        response.setTotalPrice(order.getTotalPrice());

        return ResponseEntity.ok(response);
    }

    @Data
    public static class PlaceOrderRequest {
        private String sku;
        private int quantity;
    }

    @Data
    public static class PlaceOrderResponse {
        private String orderNumber;
    }

    @Data
    public static class GetOrderResponse {
        private String orderNumber;
        private long productId;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }
}