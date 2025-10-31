package com.optivem.atddaccelerator.template.monolith.mvc.controller;

import com.optivem.atddaccelerator.template.monolith.core.services.OrderService;
import com.optivem.atddaccelerator.template.monolith.core.dtos.PlaceOrderRequest;
import com.optivem.atddaccelerator.template.monolith.core.dtos.PlaceOrderResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShopMvcController {

    private final OrderService orderService;
    
    public ShopMvcController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @PostMapping("/shop")
    public PlaceOrderResponse placeOrder(@RequestParam String sku, @RequestParam int quantity) {
        var request = new PlaceOrderRequest();
        request.setSku(sku);
        request.setQuantity(quantity);

        return orderService.placeOrder(request);
    }
}