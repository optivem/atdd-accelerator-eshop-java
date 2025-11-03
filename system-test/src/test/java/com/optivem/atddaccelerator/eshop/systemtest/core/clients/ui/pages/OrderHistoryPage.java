package com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderHistoryPage extends BasePage {
    public OrderHistoryPage(Page page, String baseUrl) {
        super(page, baseUrl);
    }

    public void inputOrderNumber(String orderNumber) {
        var orderNumberInput = page.locator("[aria-label='Order Number']");
        orderNumberInput.fill(orderNumber);
    }

    public void clickSearch() {
        var searchButton = page.locator("[aria-label='Search']");
        searchButton.click();
    }

    public void waitForOrderDetails() {
        var orderDetails = page.locator("[role='alert']");
        wait(orderDetails);

        var orderDetailsText = orderDetails.textContent();
        assertTrue(orderDetailsText.contains("Order Details"), "Should display order details heading");
    }

    public String getOrderNumber() {
        var displayOrderNumber = page.locator("[aria-label='Display Order Number']");
        return displayOrderNumber.inputValue();
    }

    public String getProductId() {
        var displayProductId = page.locator("[aria-label='Display Product ID']");
        return displayProductId.inputValue();
    }

    public String getQuantity() {
        var displayQuantity = page.locator("[aria-label='Display Quantity']");
        return displayQuantity.inputValue();
    }

    public BigDecimal getUnitPrice() {
        var displayUnitPrice = page.locator("[aria-label='Display Unit Price']");
        return getCurrencyValue(displayUnitPrice);
    }

    public BigDecimal getTotalPrice() {
        var displayTotalPrice = page.locator("[aria-label='Display Total Price']");
        return getCurrencyValue(displayTotalPrice);
    }

    public String getStatus() {
        var displayStatus = page.locator("[aria-label='Display Status']");
        return displayStatus.inputValue();
    }

    public void clickCancelOrder() {
        var cancelButton = page.locator("[aria-label='Cancel Order']");
        cancelButton.click();

        // Wait a moment for the order to be cancelled and details refreshed
        page.waitForTimeout(1000);
    }

    public void confirmCancelButtonNotVisible() {
        var cancelButton = page.locator("[aria-label='Cancel Order']");
        assertTrue(cancelButton.isHidden(), "Cancel Order button should not be visible");
    }

    private static BigDecimal getCurrencyValue(Locator locator) {
        var value = locator.inputValue();
        value = value.replace("$", "").trim();
        return new BigDecimal(value);
    }
}
