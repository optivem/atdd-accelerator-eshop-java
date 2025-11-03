package com.optivem.atddaccelerator.eshop.systemtest.e2etests;

import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;

import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.api.ApiDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApiE2eTest {
    private ApiDriver apiDriver;

    @BeforeEach
    void setUp() {
        var baseUrl = TestConfiguration.getBaseUrl();
        apiDriver = new ApiDriver(baseUrl);
    }

    @AfterEach
    void tearDown() {
        if (apiDriver != null) {
            apiDriver.close();
        }
    }

    @Test
    void placeOrder_shouldReturnOrderNumber() throws Exception {
        // Arrange
        var orderNumberAlias = "ORD-1001";
        var productId = "10";
        var quantity = "5";

        // Act
        apiDriver.placeOrder(orderNumberAlias, productId, quantity);

        // Assert
        apiDriver.confirmOrderCreated(orderNumberAlias);
        apiDriver.confirmOrderNumberGenerated(orderNumberAlias);
        apiDriver.confirmOrderNumberStartsWith(orderNumberAlias, "ORD-");
    }

    @Test
    void getOrder_shouldReturnOrderDetails() throws Exception {
        // Arrange
        var orderNumberAlias = "ORD-1002";
        var productId = "11";
        var quantity = "3";
        apiDriver.placeOrder(orderNumberAlias, productId, quantity);
        
        // Act
        apiDriver.viewOrderDetails(orderNumberAlias);

        // Assert
        apiDriver.confirmOrderDetailsExist(orderNumberAlias);
        apiDriver.confirmOrderDetailsHaveOrderNumber(orderNumberAlias);
        apiDriver.confirmOrderDetailsHaveProductId(orderNumberAlias, productId);
        apiDriver.confirmOrderDetailsHaveQuantity(orderNumberAlias, quantity);
        apiDriver.confirmOrderDetailsHavePositiveUnitPrice(orderNumberAlias);
        apiDriver.confirmOrderDetailsHavePositiveTotalPrice(orderNumberAlias);
    }

    @Test
    void cancelOrder_shouldSetStatusToCancelled() {
        // Arrange
        var orderNumberAlias = "ORD-1003";
        var productId = "12";
        var quantity = "2";
        apiDriver.placeOrder(orderNumberAlias, productId, quantity);
        
        // Act
        apiDriver.cancelOrder(orderNumberAlias);

        // Assert
        apiDriver.confirmOrderCancelled(orderNumberAlias);
        apiDriver.viewOrderDetails(orderNumberAlias);
        apiDriver.confirmOrderStatusIsCancelled(orderNumberAlias);
    }
}