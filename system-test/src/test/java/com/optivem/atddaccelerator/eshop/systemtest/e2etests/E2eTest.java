package com.optivem.atddaccelerator.eshop.systemtest.e2etests;

import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;
import com.optivem.atdd.commons.channels.Channel;
import com.optivem.atdd.commons.channels.ChannelExtension;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.ChannelType;
import com.optivem.atddaccelerator.eshop.systemtest.core.dsl.ShopDsl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ChannelExtension.class)
public class E2eTest {
    private ShopDsl shop;

    @BeforeEach
    void setUp() {
        var baseUrl = TestConfiguration.getBaseUrl();
        shop = new ShopDsl(baseUrl);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (shop != null) {
            shop.close();
        }
    }

    @Channel({ChannelType.UI, ChannelType.API})
    @TestTemplate
    void placeOrder_shouldReturnOrderNumber() {
        shop.placeOrder("orderNumber: ORD-1001", "productId: 10", "quantity: 5");
        shop.confirmOrderPlaced("orderNumber: ORD-1001", "orderNumberSuffix: ORD-");
    }

    @Channel({ChannelType.UI, ChannelType.API})
    @TestTemplate
    void getOrder_shouldReturnOrderDetails() {
        shop.placeOrder("orderNumber: ORD-1001", "productId: 11", "quantity: 3");
        shop.viewOrderDetails("orderNumber: ORD-1001");
        shop.confirmOrderDetails("orderNumber: ORD-1001", "productId: 11", "quantity: 3", "status: PLACED");
    }

    @Channel({ChannelType.UI, ChannelType.API})
    @TestTemplate
    void cancelOrder_shouldSetStatusToCancelled() {
        shop.placeOrder("orderNumber: ORD-1003", "productId: 12", "quantity: 2");
        shop.cancelOrder("orderNumber: ORD-1003");
        shop.confirmOrderCancelled("orderNumber: ORD-1003");
        shop.viewOrderDetails("orderNumber: ORD-1003");
        shop.confirmOrderStatusIsCancelled("orderNumber: ORD-1003");
    }
}
