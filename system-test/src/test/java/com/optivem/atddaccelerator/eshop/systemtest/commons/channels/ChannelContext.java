package com.optivem.atddaccelerator.eshop.systemtest.commons.channels;

import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.ChannelType;

public class ChannelContext {
    private static final ThreadLocal<String> current = new ThreadLocal<>();

    public static void set(String channel) {
        current.set(channel);
    }

    public static String get() {
        return current.get();
    }

    public static void clear() {
        current.remove();
    }
}