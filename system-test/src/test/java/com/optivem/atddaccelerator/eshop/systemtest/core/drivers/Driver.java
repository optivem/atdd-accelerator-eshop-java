package com.optivem.atddaccelerator.eshop.systemtest.core.drivers;

public interface Driver extends AutoCloseable {

    void goToShop();

    void placeOrder(String orderNumberAlias, String productId, String quantity);

    void confirmOrderCreated(String orderNumberAlias);

    void viewOrderDetails(String orderNumberAlias);

    void confirmOrderDetailsExist(String orderNumberAlias);

    void cancelOrder(String orderNumberAlias);

    void confirmOrderCancelled(String orderNumberAlias);

    void confirmOrderNumberGenerated(String orderNumberAlias);

    void confirmOrderNumberStartsWith(String orderNumberAlias, String prefix);

    void confirmOrderDetailsHaveOrderNumber(String orderNumberAlias);

    void confirmOrderDetailsHaveProductId(String orderNumberAlias, String productId);

    void confirmOrderDetailsHaveQuantity(String orderNumberAlias, String quantity);

    void confirmOrderDetailsHavePositiveUnitPrice(String orderNumberAlias);

    void confirmOrderDetailsHavePositiveTotalPrice(String orderNumberAlias);

    void confirmOrderStatusIsCancelled(String orderNumberAlias);
}
