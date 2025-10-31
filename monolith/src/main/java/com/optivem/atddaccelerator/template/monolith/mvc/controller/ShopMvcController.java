package com.optivem.atddaccelerator.template.monolith.mvc.controller;

import com.optivem.atddaccelerator.template.monolith.common.Order;
import com.optivem.atddaccelerator.template.monolith.common.OrderStorage;
import com.optivem.atddaccelerator.template.monolith.common.PriceCalculator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ShopMvcController {
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "ATDD Accelerator eShop is running!";
    }
    
    @GetMapping("/shop")
    @ResponseBody
    public String shopPage() {
        return """
            <html>
            <body>
                <form method='post' action='/shop'>
                    <input name='sku' aria-label='SKU' />
                    <input name='quantity' aria-label='Quantity' />
                    <button type='submit' aria-label='Place Order'>Place Order</button>
                </form>
                <div role='alert'></div>
            </body>
            </html>
            """;
    }

    @PostMapping("/shop")
    @ResponseBody
    public String placeOrder(@RequestParam String sku, @RequestParam int quantity) {
        var orderNumber = OrderStorage.nextOrderNumber();
        var totalPrice = PriceCalculator.calculatePrice(sku, quantity);
        var order = new Order(orderNumber, sku, quantity, totalPrice);
        OrderStorage.saveOrder(order);

        return """
            <html>
            <body>
                <form method='post' action='/shop'>
                    <input name='sku' aria-label='SKU' value='%s' />
                    <input name='quantity' aria-label='Quantity' value='%d' />
                    <button type='submit' aria-label='Place Order'>Place Order</button>
                </form>
                <div role='alert'>Success! Order has been created with Order Number %s and Total Price $%.2f</div>
            </body>
            </html>
            """.formatted(sku, quantity, orderNumber, totalPrice);
    }


}