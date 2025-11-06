package com.optivem.atddaccelerator.eshop.monolith.api.exception;

import com.optivem.atddaccelerator.eshop.monolith.core.exceptions.NotExistValidationException;
import com.optivem.atddaccelerator.eshop.monolith.core.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        var errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler(NotExistValidationException.class)
    public ResponseEntity<ErrorResponse> handleNotExistValidationException(NotExistValidationException ex) {
        return ResponseEntity.notFound().build();
    }

    public record ErrorResponse(String message) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = ex.getMessage();
        log.debug("HttpMessageNotReadableException: {}", message);

        // Check if it's related to the productId or quantity field
        // The exception message might contain field name in various forms
        if (message != null) {
            String lowerMessage = message.toLowerCase();

            // Check for productId field error
            if (lowerMessage.contains("productid") ||
                lowerMessage.contains("product_id")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Product ID must be an integer"));
            }

            // Check for various patterns that might indicate quantity field error
            if (lowerMessage.contains("quantity") ||
                lowerMessage.contains("com.optivem.atddaccelerator.eshop.monolith.core.dtos.placeorderrequest")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Quantity must be an integer"));
            }
        }

        // Also check the root cause
        Throwable cause = ex.getCause();
        if (cause != null) {
            String causeMessage = cause.getMessage();
            log.debug("Root cause: {}", causeMessage);
            if (causeMessage != null) {
                String lowerCause = causeMessage.toLowerCase();
                if (lowerCause.contains("productid")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ErrorResponse("Product ID must be an integer"));
                }
                if (lowerCause.contains("quantity")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ErrorResponse("Quantity must be an integer"));
                }
            }
        }

        // Generic parsing error
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Invalid request format"));
    }
}

