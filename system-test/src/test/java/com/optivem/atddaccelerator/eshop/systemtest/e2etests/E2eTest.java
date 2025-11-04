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
    void shouldGenerateOrderNumberWhenPlacingOrder() {
        shop.placeOrder("order: order1", "productId: 10", "quantity: 5");

        shop.confirmOrderPlaced("order: order1", "orderNumberPrefix: ORD-");
    }

    @Channel({ChannelType.UI, ChannelType.API})
    @TestTemplate
    void shouldRetainOrderDetailsAfterPlacement() {
        shop.placeOrder("order: order1", "productId: 11", "quantity: 3");

        shop.viewOrderDetails("order: order1");

        shop.confirmOrderDetails("order: order1", "productId: 11", "quantity: 3", "status: PLACED");
    }

    @Channel({ChannelType.UI, ChannelType.API})
    @TestTemplate
    void shouldAllowCancellingPlacedOrder() {
        shop.placeOrder("order: order1", "productId: 12", "quantity: 2");

        shop.cancelOrder("order: order1");

        shop.confirmOrderCancelled("order: order1");
        shop.confirmOrderStatusIsCancelled("order: order1");
    }
}
