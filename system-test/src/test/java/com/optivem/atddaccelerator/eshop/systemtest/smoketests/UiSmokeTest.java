package com.optivem.atddaccelerator.eshop.systemtest.smoketests;

import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.UiClient;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UiSmokeTest {

    private UiClient uiClient;

    @BeforeEach
    void setUp() {
        this.uiClient = new UiClient(TestConfiguration.getBaseUrl());
    }

    @AfterEach
    void tearDown() throws Exception {
        if (uiClient != null) {
            uiClient.close();
        }
    }

    @Test
    void home_shouldReturnHtmlContent() {
        uiClient.openHomePage();
    }
}
