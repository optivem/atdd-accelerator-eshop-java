package com.optivem.atddaccelerator.template.monolith.core.dtos.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductPriceResponse {
    private long id;
    private BigDecimal price;
}
