package com.optivem.atddaccelerator.template.monolith.api.controller;

import com.optivem.atddaccelerator.template.monolith.core.services.OrderService;
import com.optivem.atddaccelerator.template.monolith.core.dtos.GetOrderResponse;
import com.optivem.atddaccelerator.template.monolith.core.dtos.PlaceOrderRequest;
import com.optivem.atddaccelerator.template.monolith.core.dtos.PlaceOrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<PlaceOrderResponse> placeOrder(@RequestBody PlaceOrderRequest request) {
        var response = orderService.placeOrder(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/orders/{orderNumber}")
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable String orderNumber) {
        var response = orderService.getOrder(orderNumber);
        return ResponseEntity.ok(response);
    }
}