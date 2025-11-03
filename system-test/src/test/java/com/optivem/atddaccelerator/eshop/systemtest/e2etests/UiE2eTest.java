package com.optivem.atddaccelerator.eshop.systemtest.e2etests;

import com.microsoft.playwright.*;
import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.UiClient;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages.OrderHistoryPage;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.Driver;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.api.ApiDriver;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.ui.UiDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class UiE2eTest extends BaseE2eTest {

    @Override
    protected Driver createDriver(String baseUrl) {
        return new UiDriver(baseUrl);
    }
}