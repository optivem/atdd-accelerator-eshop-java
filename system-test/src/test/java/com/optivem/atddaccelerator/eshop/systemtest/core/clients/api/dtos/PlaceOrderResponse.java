package com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlaceOrderResponse {
    private String orderNumber;
    private BigDecimal totalPrice;
}
