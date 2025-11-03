package com.optivem.atddaccelerator.eshop.monolith.core.repositories;

import com.optivem.atddaccelerator.eshop.monolith.core.entities.Order;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepository {
    private static final HashMap<String, Order> orders = new HashMap<>();

    public void addOrder(Order order) {
        if(orders.containsKey(order.getOrderNumber())) {
            throw new IllegalArgumentException("Order with order number " + order.getOrderNumber() + " already exists.");
        }

        orders.put(order.getOrderNumber(), order);
    }

    public void updateOrder(Order order) {
        if(!orders.containsKey(order.getOrderNumber())) {
            throw new IllegalArgumentException("Order with order number " + order.getOrderNumber() + " does not exist.");
        }

        orders.put(order.getOrderNumber(), order);
    }

    public Optional<Order> getOrder(String orderNumber) {
        if(!orders.containsKey(orderNumber)) {
            return Optional.empty();
        }

        var order = orders.get(orderNumber);
        return Optional.of(order);
    }

    public String nextOrderNumber() {
        return "ORD-" + UUID.randomUUID();
    }
}
