package com.optivem.atddaccelerator.eshop.systemtest.smoketests;

import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.Driver;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.api.ApiDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class BaseSmokeTest {
    private Driver driver;

    @BeforeEach
    void setUp() {
        var baseUrl = TestConfiguration.getBaseUrl();
        this.driver = createDriver(baseUrl);
    }

    protected abstract Driver createDriver(String baseUrl);

    @AfterEach
    void tearDown() throws Exception {
        if (driver != null) {
            driver.close();
        }
    }

    @Test
    void shouldBeAbleToGoToShop() {
        driver.goToShop();
    }
}
