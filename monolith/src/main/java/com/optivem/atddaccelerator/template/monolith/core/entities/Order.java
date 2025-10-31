package com.optivem.atddaccelerator.template.monolith.core.entities;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Order {
    private String orderNumber;
    private long productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private OrderStatus status;

    public Order(String orderNumber, long productId, int quantity, BigDecimal unitPrice, BigDecimal totalPrice, OrderStatus status) {
        if (orderNumber == null) {
            throw new IllegalArgumentException("orderNumber cannot be null");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("unitPrice cannot be null");
        }
        if (totalPrice == null) {
            throw new IllegalArgumentException("totalPrice cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
        
        this.orderNumber = orderNumber;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.status = status;
    }
}
