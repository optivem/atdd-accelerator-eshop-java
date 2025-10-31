package com.optivem.atddaccelerator.template.monolith.core.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlaceOrderResponse {
    private String orderNumber;
    private BigDecimal totalPrice;
}