package com.optivem.atddaccelerator.template.monolith.core.repositories;

import com.optivem.atddaccelerator.template.monolith.core.entities.Order;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Repository
public class OrderRepository {
    // TODO: VJ: Replace with actual database
    private static final HashMap<String, Order> orders = new HashMap<>();

    public void saveOrder(Order order) {
        orders.put(order.getOrderNumber(), order);
    }

    public Order getOrder(String orderNumber) {
        return orders.get(orderNumber);
    }

    public String nextOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString();
    }
}
