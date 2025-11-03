package com.optivem.atddaccelerator.eshop.systemtest.core.drivers;

public interface Driver extends AutoCloseable {

    void goToShop();

    void placeOrder(String orderNumber, String productId, String quantity);

    void confirmOrderCreated(String orderNumber);

    void viewOrderDetails(String orderNumber);

    void confirmOrderDetailsExist(String orderNumber);

    void cancelOrder(String orderNumber);

    void confirmOrderCancelled(String orderNumber);
}
