package com.optivem.atddaccelerator.template.monolith.common;

import lombok.Data;

@Data
public class Order {
    private String orderNumber;
    private long productId;
    private int quantity;
    private double totalPrice;

    public Order(String orderNumber, long productId, int quantity, double totalPrice) {
        this.orderNumber = orderNumber;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
