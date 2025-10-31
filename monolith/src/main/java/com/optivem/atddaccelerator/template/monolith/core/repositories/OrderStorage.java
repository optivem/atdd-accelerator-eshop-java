package com.optivem.atddaccelerator.template.monolith.core.repositories;

import com.optivem.atddaccelerator.template.monolith.core.entities.Order;

import java.util.HashMap;
import java.util.UUID;

public class OrderStorage {
    // TODO: VJ: Replace with actual database
    private static final HashMap<String, Order> orders = new HashMap<>();

    public static void saveOrder(Order order) {
        orders.put(order.getOrderNumber(), order);
    }

    public static Order getOrder(String orderNumber) {
        return orders.get(orderNumber);
    }

    public static String nextOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString();
    }
}
