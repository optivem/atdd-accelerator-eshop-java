package com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetOrderResponse {
    private String orderNumber;
    private String productId;
    private String quantity;
    private String unitPrice;
    private String totalPrice;
    private String status;
}