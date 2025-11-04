package com.optivem.atddaccelerator.eshop.systemtest.smoketests;

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
public class SmokeTest {
    private ShopDsl shop;

    @BeforeEach
    void setUp() {
        var baseUrl = TestConfiguration.getBaseUrl();
        shop = ShopDsl.create(baseUrl);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (shop != null) {
            shop.close();
        }
    }

    @Channel({ChannelType.UI, ChannelType.API})
    @TestTemplate
    void shouldBeAbleToGoToShop() {
        shop.goToShop();
    }

}
