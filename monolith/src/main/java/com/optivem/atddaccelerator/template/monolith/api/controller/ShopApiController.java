package com.optivem.atddaccelerator.template.monolith.api.controller;

import com.optivem.atddaccelerator.template.monolith.common.Order;
import com.optivem.atddaccelerator.template.monolith.common.OrderStorage;
import com.optivem.atddaccelerator.template.monolith.common.PriceCalculator;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShopApiController {
    @GetMapping("/api/shop/echo")
    public ResponseEntity<Void> echo() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/shop/order")
    public ResponseEntity<PlaceOrderResponse> placeOrder(@RequestBody PlaceOrderRequest request) {
        var orderNumber = OrderStorage.nextOrderNumber();
        var sku = request.getSku();
        var quantity = request.getQuantity();
        var totalPrice = PriceCalculator.calculatePrice(sku, quantity);
        var order = new Order(orderNumber, sku, quantity, totalPrice);

        OrderStorage.saveOrder(order);

        var response = new PlaceOrderResponse();
        response.setOrderNumber(orderNumber);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/shop/order/{orderNumber}")
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable String orderNumber) {
        var order = OrderStorage.getOrder(orderNumber);
        var totalPrice = order.getTotalPrice();

        var response = new GetOrderResponse();
        response.setOrderNumber(orderNumber);
        response.setTotalPrice(totalPrice);

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
        private double totalPrice;
    }
}