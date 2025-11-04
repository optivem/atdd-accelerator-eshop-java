package com.optivem.atddaccelerator.eshop.systemtest.core.drivers;

import com.optivem.atdd.commons.channels.ChannelContext;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.api.ApiDriver;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.ui.UiDriver;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.fail;

public class SystemDriver implements Driver {
    private final HashMap<String, Driver> drivers;

    private Driver cachedActiveDriver;

    public static SystemDriver create(String baseUrl) {
        var uiDriver = new UiDriver(baseUrl);
        var apiDriver = new ApiDriver(baseUrl);

        var drivers = new HashMap<String, Driver>();
        drivers.put(ChannelType.UI, uiDriver);
        drivers.put(ChannelType.API, apiDriver);

        return new SystemDriver(drivers);
    }

    private SystemDriver(HashMap<String, Driver> drivers) {
        this.drivers = drivers;
    }

    private Driver getActiveDriver() {
        if (cachedActiveDriver != null) {
            return cachedActiveDriver;
        }

        var activeChannel = ChannelContext.get();

        if (!drivers.containsKey(activeChannel)) {
            fail("Current channel is not recognized: " + activeChannel);
        }

        cachedActiveDriver = drivers.get(activeChannel);
        return cachedActiveDriver;
    }

    @Override
    public void goToShop() {
        getActiveDriver().goToShop();
    }

    @Override
    public void placeOrder(String orderNumberAlias, String productId, String quantity) {
        getActiveDriver().placeOrder(orderNumberAlias, productId, quantity);
    }

    @Override
    public void confirmOrderPlaced(String orderNumberAlias, String prefix) {
        getActiveDriver().confirmOrderPlaced(orderNumberAlias, prefix);
    }

    @Override
    public void viewOrderDetails(String orderNumberAlias) {
        getActiveDriver().viewOrderDetails(orderNumberAlias);
    }

    @Override
    public void confirmOrderDetails(String orderNumberAlias, String productId, String quantity, String status) {
        getActiveDriver().confirmOrderDetails(orderNumberAlias, productId, quantity, status);
    }

    @Override
    public void cancelOrder(String orderNumberAlias) {
        getActiveDriver().cancelOrder(orderNumberAlias);
    }

    @Override
    public void confirmOrderCancelled(String orderNumberAlias) {
        getActiveDriver().confirmOrderCancelled(orderNumberAlias);
    }

    @Override
    public void confirmOrderStatusIsCancelled(String orderNumberAlias) {
        getActiveDriver().confirmOrderStatusIsCancelled(orderNumberAlias);
    }

    @Override
    public void close() throws Exception {
        Exception firstEx = null;

        for (Driver driver : drivers.values()) {
            if (driver == null) {
                continue;
            }
            try {
                driver.close();
            } catch (Exception e) {
                if (firstEx == null) {
                    firstEx = e;
                } else {
                    firstEx.addSuppressed(e);
                }
            }
        }

        cachedActiveDriver = null;

        if (firstEx != null) {
            throw firstEx;
        }
    }
}
