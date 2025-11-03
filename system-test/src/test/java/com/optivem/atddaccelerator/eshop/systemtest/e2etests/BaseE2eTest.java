package com.optivem.atddaccelerator.eshop.systemtest.e2etests;

import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.Driver;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.api.ApiDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class BaseE2eTest {
    private Driver driver;

    public BaseE2eTest(Driver driver) {
        this.driver = driver;
    }

    @BeforeEach
    void setUp() {
        var baseUrl = TestConfiguration.getBaseUrl();
        driver = new ApiDriver(baseUrl);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (driver != null) {
            driver.close();
        }
    }

    @Test
    void placeOrder_shouldReturnOrderNumber() throws Exception {
        // Arrange
        var orderNumberAlias = "ORD-1001";
        var productId = "10";
        var quantity = "5";

        // Act
        driver.placeOrder(orderNumberAlias, productId, quantity);

        // Assert
        driver.confirmOrderPlaced(orderNumberAlias, "ORD-");
    }

    @Test
    void getOrder_shouldReturnOrderDetails() throws Exception {
        // Arrange
        var orderNumberAlias = "ORD-1002";
        var productId = "11";
        var quantity = "3";
        driver.placeOrder(orderNumberAlias, productId, quantity);

        // Act
        driver.viewOrderDetails(orderNumberAlias);

        // Assert
        driver.confirmOrderDetails(orderNumberAlias, productId, quantity, "PLACED");
    }

    @Test
    void cancelOrder_shouldSetStatusToCancelled() {
        // Arrange
        var orderNumberAlias = "ORD-1003";
        var productId = "12";
        var quantity = "2";
        driver.placeOrder(orderNumberAlias, productId, quantity);

        // Act
        driver.cancelOrder(orderNumberAlias);

        // Assert
        driver.confirmOrderCancelled(orderNumberAlias);
        driver.viewOrderDetails(orderNumberAlias);
        driver.confirmOrderStatusIsCancelled(orderNumberAlias);
    }
}
