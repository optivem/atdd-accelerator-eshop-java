package com.optivem.atddaccelerator.eshop.systemtest.core.dsl;

import com.optivem.atddaccelerator.eshop.systemtest.commons.dsl.DslContext;
import com.optivem.atddaccelerator.eshop.systemtest.commons.dsl.DslParamsFactory;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.SystemDriver;

public class ShopDsl implements AutoCloseable {
    private final DslParamsFactory paramsFactory;
    private final SystemDriver driver;

    public ShopDsl(String baseUrl) {
        this.driver = new SystemDriver(baseUrl);
        var context = new DslContext();
        this.paramsFactory = new DslParamsFactory(context);
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void goToShop() {
        driver.goToShop();
    }

    public void placeOrder(String... args) {
        var params = paramsFactory.create(args);
        var orderNumber = params.getAlias("orderNumber");
        var productId = params.getValue("productId", "1");
        var quantity = params.getValue("quantity", "20");
        driver.placeOrder(orderNumber, productId, quantity);
    }

    public void confirmOrderPlaced(String... args) {
        var params = paramsFactory.create(args);
        var orderNumber = params.getAlias("orderNumber");
        var suffix = params.getValue("orderNumberSuffix", "");
        driver.confirmOrderPlaced(orderNumber, suffix);
    }

    public void viewOrderDetails(String... args) {
        var params = paramsFactory.create(args);
        var orderNumber = params.getAlias("orderNumber");
        driver.viewOrderDetails(orderNumber);
    }

    public void confirmOrderDetails(String... args) {
        var params = paramsFactory.create(args);
        var orderNumber = params.getAlias("orderNumber");
        var productId = params.getValue("productId", "1");
        var quantity = params.getValue("quantity", "20");
        var status = params.getValue("status", "PLACED");
        driver.confirmOrderDetails(orderNumber, productId, quantity, status);
    }

    public void cancelOrder(String... args) {
        var params = paramsFactory.create(args);
        var orderNumber = params.getAlias("orderNumber");
        driver.cancelOrder(orderNumber);
    }

    public void confirmOrderCancelled(String... args) {
        var params = paramsFactory.create(args);
        var orderNumber = params.getAlias("orderNumber");
        driver.confirmOrderCancelled(orderNumber);
    }

    public void confirmOrderStatusIsCancelled(String... args) {
        var params = paramsFactory.create(args);
        var orderNumber = params.getAlias("orderNumber");
        driver.confirmOrderStatusIsCancelled(orderNumber);
    }
}
