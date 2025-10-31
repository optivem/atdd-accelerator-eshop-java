package com.optivem.atddaccelerator.eshop.monolith.core.services;

import com.optivem.atddaccelerator.eshop.monolith.core.entities.Order;
import com.optivem.atddaccelerator.eshop.monolith.core.entities.OrderStatus;
import com.optivem.atddaccelerator.eshop.monolith.core.exceptions.ValidationException;
import com.optivem.atddaccelerator.eshop.monolith.core.repositories.OrderRepository;
import com.optivem.atddaccelerator.eshop.monolith.core.services.external.ErpGateway;
import com.optivem.atddaccelerator.eshop.monolith.core.dtos.GetOrderResponse;
import com.optivem.atddaccelerator.eshop.monolith.core.dtos.PlaceOrderRequest;
import com.optivem.atddaccelerator.eshop.monolith.core.dtos.PlaceOrderResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;

@Service
public class OrderService {

    public static final MonthDay DECEMBER_31 = MonthDay.of(12, 31);
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

        orderRepository.addOrder(order);

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
        var now = LocalDateTime.now();
        var currentDate = MonthDay.from(now);
        var currentTime = now.toLocalTime();

        if (currentDate.equals(DECEMBER_31) &&
            currentTime.isAfter(CANCELLATION_BLOCK_START) && 
            currentTime.isBefore(CANCELLATION_BLOCK_END)) {
            throw new ValidationException("Order cancellation is not allowed on December 31st between 22:00 and 23:00");
        }
        
        var order = orderRepository.getOrder(orderNumber);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.updateOrder(order);
    }
}
