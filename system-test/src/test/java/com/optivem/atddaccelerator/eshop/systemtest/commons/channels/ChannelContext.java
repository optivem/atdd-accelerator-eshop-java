package com.optivem.atddaccelerator.eshop.systemtest.commons.channels;

import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.ChannelType;

public class ChannelContext {
    private static final ThreadLocal<String> current = new ThreadLocal<>();

    public static void set(String channel) {
        current.set(channel);
    }

    public static String get() {
        var channel = current.get();

        if(channel == null) {
            throw new RuntimeException("Channel type is not set. Please ensure that the test class is annotated with @ExtendWith(ChannelExtension.class) and that test methods are annotated with @Channel and @TestTemplate");
        }

        return channel;
    }

    public static void clear() {
        current.remove();
    }
}