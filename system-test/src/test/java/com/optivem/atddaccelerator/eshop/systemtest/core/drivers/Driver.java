package com.optivem.atddaccelerator.eshop.systemtest.core.drivers;

public interface Driver extends AutoCloseable {
    void goToShop();

    void placeOrder(String order, String productId, String quantity);

    void confirmOrderPlaced(String order, String prefix);

    void viewOrderDetails(String order);

    void confirmOrderDetails(String order, String productId, String quantity, String status);

    void cancelOrder(String order);

    void confirmOrderCancelled(String order);

    void confirmOrderStatusIsCancelled(String order);
}
