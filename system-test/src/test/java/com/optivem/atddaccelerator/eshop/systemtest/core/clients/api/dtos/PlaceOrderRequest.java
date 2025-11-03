package com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.dtos;

import lombok.Data;

@Data
public class PlaceOrderRequest {
    private String productId;
    private String quantity;
}
