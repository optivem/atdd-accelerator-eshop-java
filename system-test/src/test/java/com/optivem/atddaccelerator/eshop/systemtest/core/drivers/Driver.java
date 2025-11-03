package com.optivem.atddaccelerator.eshop.systemtest.core.drivers;

public interface Driver extends AutoCloseable {

    void goToShop();

    void placeOrder(String orderNumberAlias, String productId, String quantity);

    void confirmOrderPlaced(String orderNumberAlias, String prefix);

    void viewOrderDetails(String orderNumberAlias);

    void confirmOrderDetails(String orderNumberAlias, String productId, String quantity, String status);

    void cancelOrder(String orderNumberAlias);

    void confirmOrderCancelled(String orderNumberAlias);

    void confirmOrderStatusIsCancelled(String orderNumberAlias);
}
