package com.optivem.atddaccelerator.eshop.systemtest.smoketests;

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
public class SmokeTest {
    private DriverFactory driverFactory;
    private Driver driver;

    @BeforeEach
    void setUp() {
        var baseUrl = TestConfiguration.getBaseUrl();
        this.driverFactory = new DriverFactory(baseUrl);
        this.driver = driverFactory.createDriver();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (driver != null) {
            driver.close();
        }
    }

    @Channel({ChannelType.UI, ChannelType.API})
    @TestTemplate
    void shouldBeAbleToGoToShop() {
        driver.goToShop();
    }
}
