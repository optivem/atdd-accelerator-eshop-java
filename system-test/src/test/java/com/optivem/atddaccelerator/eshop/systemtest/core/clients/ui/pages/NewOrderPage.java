package com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages;

import com.microsoft.playwright.Page;

public class NewOrderPage extends BasePage {
    public NewOrderPage(Page page, String baseUrl) {
        super(page, baseUrl);
    }

    public void inputProductId(String productId) {
        fill("[aria-label=\"Product ID\"]", productId);
    }

    public void inputQuantity(String quantity) {
        fill("[aria-label=\"Quantity\"]", quantity);
    }

    public void clickPlaceOrder() {
        click("[aria-label=\"Place Order\"]");
    }

    public String readConfirmationMessageText() {
        return readTextContent("[role='alert']");
    }
}
