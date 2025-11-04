package com.optivem.atddaccelerator.eshop.systemtest.core.drivers;

import com.optivem.atdd.commons.channels.ChannelContext;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.api.ApiDriver;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.ui.UiDriver;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.fail;

public class SystemDriver implements Driver {
    private final HashMap<String, Driver> drivers;

    private Driver cachedActiveDriver;

    public SystemDriver(String baseUrl) {
        var uiDriver = new UiDriver(baseUrl);
        var apiDriver = new ApiDriver(baseUrl);

        this.drivers = new HashMap<>();
        this.drivers.put(ChannelType.UI, uiDriver);
        this.drivers.put(ChannelType.API, apiDriver);
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
    public void placeOrder(String order, String productId, String quantity) {
        getActiveDriver().placeOrder(order, productId, quantity);
    }

    @Override
    public void confirmOrderPlaced(String order, String prefix) {
        getActiveDriver().confirmOrderPlaced(order, prefix);
    }

    @Override
    public void viewOrderDetails(String order) {
        getActiveDriver().viewOrderDetails(order);
    }

    @Override
    public void confirmOrderDetails(String order, String productId, String quantity, String status) {
        getActiveDriver().confirmOrderDetails(order, productId, quantity, status);
    }

    @Override
    public void cancelOrder(String order) {
        getActiveDriver().cancelOrder(order);
    }

    @Override
    public void confirmOrderCancelled(String order) {
        getActiveDriver().confirmOrderCancelled(order);
    }

    @Override
    public void confirmOrderStatusIsCancelled(String order) {
        getActiveDriver().confirmOrderStatusIsCancelled(order);
    }

    @Override
    public void confirmOrderPlacementFailed(String order, String errorMessage) {
        getActiveDriver().confirmOrderPlacementFailed(order, errorMessage);
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
