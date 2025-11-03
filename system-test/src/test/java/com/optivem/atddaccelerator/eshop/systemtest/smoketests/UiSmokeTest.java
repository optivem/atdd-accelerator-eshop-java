package com.optivem.atddaccelerator.eshop.systemtest.smoketests;

import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.UiClient;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.ui.UiDriver;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UiSmokeTest {

    private UiDriver uiDriver;

    @BeforeEach
    void setUp() {
        var baseUrl = TestConfiguration.getBaseUrl();
        this.uiDriver = new UiDriver(baseUrl);
    }

    @AfterEach
    void tearDown() {
        if (uiDriver != null) {
            uiDriver.close();
        }
    }

    @Test
    void home_shouldReturnHtmlContent() {
        uiDriver.goToShop();
    }
}
