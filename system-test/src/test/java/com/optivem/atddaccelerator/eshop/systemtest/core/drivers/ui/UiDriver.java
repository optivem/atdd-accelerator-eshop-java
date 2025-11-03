package com.optivem.atddaccelerator.eshop.systemtest.core.drivers.ui;

import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.UiClient;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages.HomePage;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages.NewOrderPage;
import com.optivem.atddaccelerator.eshop.systemtest.core.drivers.Driver;

public class UiDriver implements Driver {

    private final UiClient client;

    private HomePage homePage;
    private NewOrderPage newOrderPage;

    public UiDriver(String baseUrl) {
        this.client = new UiClient(baseUrl);
    }

    @Override
    public void goToShop() {
        homePage = client.openHomePage();
        newOrderPage = homePage.clickNewOrder();
    }

    @Override
    public void placeOrder(String orderNumber, String productId, String quantity) {
        newOrderPage.inputProductId(productId);
        newOrderPage.inputQuantity(quantity);
        newOrderPage.clickPlaceOrder();
    }

    @Override
    public void confirmOrderCreated(String orderNumber) {

    }

    @Override
    public void viewOrderDetails(String orderNumber) {

    }

    @Override
    public void confirmOrderDetailsExist(String orderNumber) {

    }

    @Override
    public void cancelOrder(String orderNumber) {

    }

    @Override
    public void confirmOrderCancelled(String orderNumber) {

    }

    @Override
    public void close() {
        client.close();
    }
}
