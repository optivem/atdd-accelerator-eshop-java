package com.optivem.atddaccelerator.eshop.systemtest.core.drivers;

import com.optivem.atddaccelerator.eshop.systemtest.commons.channels.ChannelContext;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.api.ApiDriver;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.ui.UiDriver;

import java.util.Objects;

public class DriverFactory {

    private String baseUrl;

    public DriverFactory(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Driver createDriver() {
        var channelType = ChannelContext.get();

        if(Objects.equals(channelType, ChannelType.UI)) {
            return new UiDriver(baseUrl);
        }
        else if(Objects.equals(channelType, ChannelType.API)) {
            return new ApiDriver(baseUrl);
        } else {
            throw new RuntimeException("Unsupported channel: " + channelType);
        }
    }
}
