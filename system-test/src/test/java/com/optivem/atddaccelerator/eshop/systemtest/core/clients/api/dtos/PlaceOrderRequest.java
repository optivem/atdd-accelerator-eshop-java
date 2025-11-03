package com.optivem.atddaccelerator.eshop.systemtest.core.clients.api.dtos;

import lombok.Data;

@Data
public class PlaceOrderRequest {
    private long productId;
    private int quantity;
}
