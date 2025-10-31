package com.optivem.atddaccelerator.template.monolith.core.services;

import com.optivem.atddaccelerator.template.monolith.core.entities.Order;
import com.optivem.atddaccelerator.template.monolith.core.repositories.OrderRepository;
import com.optivem.atddaccelerator.template.monolith.core.services.external.ErpGateway;
import com.optivem.atddaccelerator.template.monolith.core.dtos.GetOrderResponse;
import com.optivem.atddaccelerator.template.monolith.core.dtos.PlaceOrderRequest;
import com.optivem.atddaccelerator.template.monolith.core.dtos.PlaceOrderResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ErpGateway erpGateway;

    public OrderService(OrderRepository orderRepository, ErpGateway erpGateway) {
        this.orderRepository = orderRepository;
        this.erpGateway = erpGateway;
    }

    public PlaceOrderResponse placeOrder(PlaceOrderRequest request) {
        var orderNumber = orderRepository.nextOrderNumber();
        var productId = request.getProductId();
        var quantity = request.getQuantity();
        var unitPrice = erpGateway.getUnitPrice(productId);
        var totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        var order = new Order(orderNumber, productId, quantity, unitPrice, totalPrice);

        orderRepository.saveOrder(order);

        var response = new PlaceOrderResponse();
        response.setOrderNumber(orderNumber);
        response.setTotalPrice(totalPrice);
        return response;
    }

    public GetOrderResponse getOrder(String orderNumber) {
        var order = orderRepository.getOrder(orderNumber);

        var response = new GetOrderResponse();
        response.setOrderNumber(orderNumber);
        response.setProductId(order.getProductId());
        response.setQuantity(order.getQuantity());
        response.setUnitPrice(order.getUnitPrice());
        response.setTotalPrice(order.getTotalPrice());

        return response;
    }
}
