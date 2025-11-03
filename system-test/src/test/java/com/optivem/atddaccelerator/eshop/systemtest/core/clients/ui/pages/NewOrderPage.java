package com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class NewOrderPage extends BasePage {
    public NewOrderPage(Page page, String baseUrl) {
        super(page, baseUrl);
    }

    public void inputProductId(String productId) {
        var productIdInput = page.locator("[aria-label='Product ID']");
        productIdInput.fill(productId);
    }

    public void inputQuantity(String quantity) {
        var quantityInput = page.locator("[aria-label='Quantity']");
        quantityInput.fill(quantity);
    }

    public void clickPlaceOrder() {
        var placeOrderButton = page.locator("[aria-label='Place Order']");
        placeOrderButton.click();
    }

    public String readConfirmationMessageText() {
        var confirmationMessage = page.locator("[role='alert']");
        wait(confirmationMessage);
        return confirmationMessage.textContent();
    }
}
