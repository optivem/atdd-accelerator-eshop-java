package com.optivem.atddaccelerator.template.monolith.common;

import java.util.HashMap;
import java.util.UUID;

public class OrderStorage {
    // TODO: VJ: Replace with actual database
    private static final HashMap<String, Order> orders = new HashMap<>();
    private static Order latestOrder = null;

    public static void saveOrder(Order order) {
        orders.put(order.getOrderNumber(), order);
        latestOrder = order;
    }

    public static Order getOrder(String orderNumber) {
        return orders.get(orderNumber);
    }
    
    public static Order getLatestOrder() {
        return latestOrder;
    }

    public static String nextOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString();
    }
}
