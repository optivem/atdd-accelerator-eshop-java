package com.optivem.atddaccelerator.template.monolith.api.controller;

import com.optivem.atddaccelerator.template.monolith.core.exceptions.OrderCancellationNotAllowedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderCancellationNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleOrderCancellationNotAllowed(OrderCancellationNotAllowedException ex) {
        var errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    public record ErrorResponse(String message) {}
}
