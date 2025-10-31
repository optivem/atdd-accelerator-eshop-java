package com.optivem.atddaccelerator.template.monolith.core.exceptions;

public class OrderCancellationNotAllowedException extends RuntimeException {
    public OrderCancellationNotAllowedException(String message) {
        super(message);
    }
}
