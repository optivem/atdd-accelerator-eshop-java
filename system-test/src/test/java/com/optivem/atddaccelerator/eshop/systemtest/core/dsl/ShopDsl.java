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

    public void goToShop() {
        driver.goToShop();
    }

    public void placeOrder(String... args) {
        driver.goToShop();
        var params = paramsFactory.create(args);
        var orderNumber = params.getAlias("orderNumber");
        var productId = params.getAlias("productId");
        var quantity = params.getValue("quantity", "20");
        driver.placeOrder(orderNumber, productId, quantity);
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

//    public void confirmOrderTotalPrice(String... args) {
//        var params = paramsFactory.create(args);
//        var orderNumber = params.getAlias("orderNumber");
//        var expectedPrice = params.getValue("totalPrice", "150.00");
//        driver.confirmOrderTotalPrice(orderNumber, expectedPrice);
//    }
//
//    public void confirmOrderNumberGenerated(String... args) {
//        var params = paramsFactory.create(args);
//        var orderNumber = params.getAlias("orderNumber");
//
//        driver.confirmOrderNumberGenerated(orderNumber);
//    }
}
