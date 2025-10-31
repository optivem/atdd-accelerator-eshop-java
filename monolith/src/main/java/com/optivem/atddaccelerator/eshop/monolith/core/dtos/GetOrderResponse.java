package com.optivem.atddaccelerator.eshop.monolith.core.dtos;

import com.optivem.atddaccelerator.eshop.monolith.core.entities.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetOrderResponse {
    private String orderNumber;
    private long productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private OrderStatus status;
}