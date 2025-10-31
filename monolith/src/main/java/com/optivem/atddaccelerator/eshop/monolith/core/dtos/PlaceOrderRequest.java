package com.optivem.atddaccelerator.eshop.monolith.core.dtos;

import lombok.Data;

@Data
public class PlaceOrderRequest {
    private long productId;
    private int quantity;
}