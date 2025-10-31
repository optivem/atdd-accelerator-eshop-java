package com.optivem.atddaccelerator.template.monolith.core.dtos;

import lombok.Data;

@Data
public class PlaceOrderRequest {
    private String sku;
    private int quantity;
}