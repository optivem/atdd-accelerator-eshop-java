package com.optivem.atddaccelerator.eshop.systemtest.e2etests;

import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;

import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.Driver;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.api.ApiDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApiE2eTest extends BaseE2eTest {

    public ApiE2eTest() {
        super(createDriver());
    }

    private static Driver createDriver() {
        var baseUrl = TestConfiguration.getBaseUrl();
        return new ApiDriver(baseUrl);
    }
}