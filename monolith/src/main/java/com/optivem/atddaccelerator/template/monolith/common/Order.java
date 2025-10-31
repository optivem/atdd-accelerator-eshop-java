package com.optivem.atddaccelerator.template.monolith.common;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Order {
    private String orderNumber;
    private long productId;
    private int quantity;
    private BigDecimal totalPrice;

    public Order(String orderNumber, long productId, int quantity, BigDecimal totalPrice) {
        this.orderNumber = orderNumber;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
