package com.optivem.atddaccelerator.template.monolith.mvc.controller;

import com.optivem.atddaccelerator.template.monolith.common.Order;
import com.optivem.atddaccelerator.template.monolith.common.OrderStorage;
import com.optivem.atddaccelerator.template.monolith.common.PriceCalculator;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShopMvcController {
    
    @PostMapping("/shop")
    public Order placeOrder(@RequestParam String sku, @RequestParam int quantity) {
        var orderNumber = OrderStorage.nextOrderNumber();
        var totalPrice = PriceCalculator.calculatePrice(sku, quantity);
        var order = new Order(orderNumber, sku, quantity, totalPrice);
        OrderStorage.saveOrder(order);
        
        return order;
    }
}