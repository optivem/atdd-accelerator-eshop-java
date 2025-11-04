package com.optivem.atddaccelerator.eshop.systemtest.e2etests;

import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;
import com.optivem.atddaccelerator.eshop.systemtest.commons.channels.Channel;
import com.optivem.atddaccelerator.eshop.systemtest.commons.channels.ChannelExtension;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.ChannelType;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.Driver;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.DriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ChannelExtension.class)
public class E2eTest {
    private Driver driver;

    @BeforeEach
    void setUp() {
        var baseUrl = TestConfiguration.getBaseUrl();
        var driverFactory = new DriverFactory(baseUrl);
        driver = driverFactory.createDriver();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (driver != null) {
            driver.close();
        }
    }

    @Channel({ChannelType.UI, ChannelType.API})
    @TestTemplate
    void placeOrder_shouldReturnOrderNumber() {
        // Arrange
        var orderNumberAlias = "ORD-1001";
        var productId = "10";
        var quantity = "5";

        // Act
        driver.placeOrder(orderNumberAlias, productId, quantity);

        // Assert
        driver.confirmOrderPlaced(orderNumberAlias, "ORD-");
    }

    @Channel({ChannelType.UI, ChannelType.API})
    @TestTemplate
    void getOrder_shouldReturnOrderDetails() {
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

    @Channel({ChannelType.UI, ChannelType.API})
    @TestTemplate
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
