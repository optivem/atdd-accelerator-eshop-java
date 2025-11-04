package com.optivem.atddaccelerator.eshop.systemtest.commons.channels;

import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.ChannelType;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@TestTemplate
@ExtendWith(ChannelExtension.class)
public @interface Channel {
    String[] value();
}