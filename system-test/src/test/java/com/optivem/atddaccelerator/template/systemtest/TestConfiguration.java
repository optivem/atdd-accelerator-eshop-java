package com.optivem.atddaccelerator.template.systemtest;

import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class TestConfiguration {
    
    private static final Map<String, Object> config;
    
    static {
        var yaml = new Yaml();
        var inputStream = TestConfiguration.class
                .getClassLoader()
                .getResourceAsStream("application.yml");
        config = yaml.load(inputStream);
    }
    
    @SuppressWarnings("unchecked")
    private static <T> T getNestedValue(String... keys) {
        var current = config;
        for (int i = 0; i < keys.length - 1; i++) {
            current = (Map<String, Object>) current.get(keys[i]);
        }
        return (T) current.get(keys[keys.length - 1]);
    }
    
    public static int getServerPort() {
        return getNestedValue("test", "server", "port");
    }
    
    public static String getErpUrlE2e() {
        return getNestedValue("test", "erp", "url", "e2e");
    }
    
    public static String getErpUrlAcc() {
        return getNestedValue("test", "erp", "url", "acc");
    }
    
    public static int getWaitSeconds() {
        return getNestedValue("test", "wait", "seconds");
    }
}
