package com.optivem.atddaccelerator.template.monolith.mvc.controller;

import com.optivem.atddaccelerator.template.monolith.common.Order;
import com.optivem.atddaccelerator.template.monolith.common.OrderStorage;
import com.optivem.atddaccelerator.template.monolith.common.PriceCalculator;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShopMvcController {
    
    private final PriceCalculator priceCalculator;
    
    public ShopMvcController(PriceCalculator priceCalculator) {
        this.priceCalculator = priceCalculator;
    }
    
    @PostMapping("/shop")
    public Order placeOrder(@RequestParam String sku, @RequestParam int quantity) {
        var orderNumber = OrderStorage.nextOrderNumber();
        var productId = Long.parseLong(sku);
        var totalPrice = priceCalculator.calculatePrice(productId, quantity);
        var order = new Order(orderNumber, productId, quantity, totalPrice);
        OrderStorage.saveOrder(order);
        
        return order;
    }
}