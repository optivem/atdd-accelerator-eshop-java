package com.optivem.atddaccelerator.template.monolith.core.services;

import com.optivem.atddaccelerator.template.monolith.core.entities.Order;
import com.optivem.atddaccelerator.template.monolith.core.entities.OrderStatus;
import com.optivem.atddaccelerator.template.monolith.core.exceptions.ValidationException;
import com.optivem.atddaccelerator.template.monolith.core.repositories.OrderRepository;
import com.optivem.atddaccelerator.template.monolith.core.services.external.ErpGateway;
import com.optivem.atddaccelerator.template.monolith.core.dtos.GetOrderResponse;
import com.optivem.atddaccelerator.template.monolith.core.dtos.PlaceOrderRequest;
import com.optivem.atddaccelerator.template.monolith.core.dtos.PlaceOrderResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;

@Service
public class OrderService {

    private static final LocalTime CANCELLATION_BLOCK_START = LocalTime.of(22, 0);
    private static final LocalTime CANCELLATION_BLOCK_END = LocalTime.of(23, 0);

    private final OrderRepository orderRepository;
    private final ErpGateway erpGateway;

    public OrderService(OrderRepository orderRepository, ErpGateway erpGateway) {
        this.orderRepository = orderRepository;
        this.erpGateway = erpGateway;
    }

    public PlaceOrderResponse placeOrder(PlaceOrderRequest request) {
        var productId = request.getProductId();
        var quantity = request.getQuantity();
        
        if (productId <= 0) {
            throw new ValidationException("Product ID must be greater than 0, received: " + productId);
        }
        if (quantity <= 0) {
            throw new ValidationException("Quantity must be greater than 0, received: " + quantity);
        }
        
        var orderNumber = orderRepository.nextOrderNumber();
        var unitPrice = erpGateway.getUnitPrice(productId);
        var totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        var order = new Order(orderNumber, productId, quantity, unitPrice, totalPrice, OrderStatus.PLACED);

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
        response.setStatus(order.getStatus());

        return response;
    }

    public void cancelOrder(String orderNumber) {
        var currentTime = LocalTime.now();

        if (!currentTime.isBefore(CANCELLATION_BLOCK_START) && currentTime.isBefore(CANCELLATION_BLOCK_END)) {
            throw new ValidationException("Order cancellation is not allowed between 10:00 and 11:00");
        }
        
        var order = orderRepository.getOrder(orderNumber);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.saveOrder(order);
    }
}
