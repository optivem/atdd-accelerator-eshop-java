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
    public void placeOrder(String orderNumberAlias, String productId, String quantity) {
        newOrderPage.inputProductId(productId);
        newOrderPage.inputQuantity(quantity);
        newOrderPage.clickPlaceOrder();
    }

    @Override
    public void confirmOrderCreated(String orderNumberAlias) {

    }

    @Override
    public void viewOrderDetails(String orderNumberAlias) {

    }

    @Override
    public void confirmOrderDetailsExist(String orderNumberAlias) {

    }

    @Override
    public void cancelOrder(String orderNumberAlias) {

    }

    @Override
    public void confirmOrderCancelled(String orderNumberAlias) {

    }

    @Override
    public void confirmOrderNumberGenerated(String orderNumberAlias) {

    }

    @Override
    public void confirmOrderNumberStartsWith(String orderNumberAlias, String prefix) {

    }

    @Override
    public void confirmOrderDetailsHaveOrderNumber(String orderNumberAlias) {

    }

    @Override
    public void confirmOrderDetailsHaveProductId(String orderNumberAlias, String productId) {

    }

    @Override
    public void confirmOrderDetailsHaveQuantity(String orderNumberAlias, String quantity) {

    }

    @Override
    public void confirmOrderDetailsHavePositiveUnitPrice(String orderNumberAlias) {

    }

    @Override
    public void confirmOrderDetailsHavePositiveTotalPrice(String orderNumberAlias) {

    }

    @Override
    public void confirmOrderStatusIsCancelled(String orderNumberAlias) {

    }

    @Override
    public void close() {
        client.close();
    }
}
