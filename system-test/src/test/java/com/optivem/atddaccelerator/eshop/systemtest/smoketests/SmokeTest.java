package com.optivem.atddaccelerator.eshop.systemtest.smoketests;

import com.optivem.atdd.commons.MathCommons;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ChannelExtension.class)
public class SmokeTest {
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
    void shouldBeAbleToGoToShop() {
        driver.goToShop();
    }


    @Channel({ChannelType.UI, ChannelType.API})
    @TestTemplate
    void shouldPerformMathAddition() {
        int result = MathCommons.addition(5, 3);
        System.out.println("Result: " + result); // Output: Result: 8
        assertEquals(8, result, "Expected 5 + 3 to equal 8");

        var result2 = MathCommons.subtraction(5, 6);
    }

}
