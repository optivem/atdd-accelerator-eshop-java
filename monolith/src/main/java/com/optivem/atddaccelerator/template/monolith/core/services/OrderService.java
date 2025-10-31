package com.optivem.atddaccelerator.template.monolith.core.services;

import com.optivem.atddaccelerator.template.monolith.core.entities.Order;
import com.optivem.atddaccelerator.template.monolith.core.repositories.OrderStorage;
import com.optivem.atddaccelerator.template.monolith.core.dtos.GetOrderResponse;
import com.optivem.atddaccelerator.template.monolith.core.dtos.PlaceOrderRequest;
import com.optivem.atddaccelerator.template.monolith.core.dtos.PlaceOrderResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final ErpGateway erpGateway;

    public OrderService(ErpGateway erpGateway) {
        this.erpGateway = erpGateway;
    }

    public PlaceOrderResponse placeOrder(PlaceOrderRequest request) {
        var orderNumber = OrderStorage.nextOrderNumber();
        var sku = request.getSku();
        var quantity = request.getQuantity();
        var productId = Long.parseLong(sku);
        BigDecimal unitPrice = erpGateway.getUnitPrice(productId);
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        var order = new Order(orderNumber, productId, quantity, unitPrice, totalPrice);

        OrderStorage.saveOrder(order);

        var response = new PlaceOrderResponse();
        response.setOrderNumber(orderNumber);
        response.setTotalPrice(totalPrice);
        return response;
    }

    public GetOrderResponse getOrder(String orderNumber) {
        var order = OrderStorage.getOrder(orderNumber);

        var response = new GetOrderResponse();
        response.setOrderNumber(orderNumber);
        response.setProductId(order.getProductId());
        response.setQuantity(order.getQuantity());
        response.setUnitPrice(order.getUnitPrice());
        response.setTotalPrice(order.getTotalPrice());

        return response;
    }
}
