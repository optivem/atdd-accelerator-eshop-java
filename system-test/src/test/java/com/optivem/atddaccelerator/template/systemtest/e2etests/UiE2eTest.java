package com.optivem.atddaccelerator.template.systemtest.e2etests;

import com.microsoft.playwright.*;
import com.optivem.atddaccelerator.template.systemtest.TestConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

class UiE2eTest {
    
    private Playwright playwright;
    private Browser browser;
    private Page page;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        page = browser.newPage();
        baseUrl = "http://localhost:" + TestConfiguration.getServerPort();
    }

    @AfterEach
    void tearDown() {
        if (page != null) {
            page.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    void shouldCalculateTotalOrderPrice() {
        // Act
        page.navigate(baseUrl + "/shop.html");

        var productIdInput = page.locator("[aria-label='Product ID']");
        productIdInput.fill("10");

        var quantityInput = page.locator("[aria-label='Quantity']");
        quantityInput.fill("5");

        var placeOrderButton = page.locator("[aria-label='Place Order']");
        placeOrderButton.click();

        // Wait for confirmation message to appear
        var confirmationMessage = page.locator("[role='alert']");
        confirmationMessage.waitFor(new Locator.WaitForOptions().setTimeout(TestConfiguration.getWaitSeconds() * 1000));

        var confirmationMessageText = confirmationMessage.textContent();

        var pattern = Pattern.compile("Success! Order has been created with Order Number ([\\w-]+) and Total Price \\$(\\d+(?:\\.\\d{2})?)");
        var matcher = pattern.matcher(confirmationMessageText);

        assertTrue(matcher.find(), "Confirmation message should match expected pattern. Actual: " + confirmationMessageText);

        var totalPriceString = matcher.group(2);
        var totalPrice = Double.parseDouble(totalPriceString);
        assertTrue(totalPrice > 0, "Total price should be positive. Actual: " + totalPrice);
    }

    @Test
    void shouldRetrieveOrderHistory() {
        // Arrange - First place an order to get an order number
        page.navigate(baseUrl + "/shop.html");

        var productIdInput = page.locator("[aria-label='Product ID']");
        productIdInput.fill("11");

        var quantityInput = page.locator("[aria-label='Quantity']");
        quantityInput.fill("3");

        var placeOrderButton = page.locator("[aria-label='Place Order']");
        placeOrderButton.click();

        // Wait for confirmation message and extract order number
        var confirmationMessage = page.locator("[role='alert']");
        confirmationMessage.waitFor(new Locator.WaitForOptions().setTimeout(TestConfiguration.getWaitSeconds() * 1000));

        var confirmationMessageText = confirmationMessage.textContent();
        var pattern = Pattern.compile("Success! Order has been created with Order Number ([\\w-]+)");
        var matcher = pattern.matcher(confirmationMessageText);
        assertTrue(matcher.find(), "Should extract order number from confirmation message");
        var orderNumber = matcher.group(1);

        // Act - Navigate to Order History and search for the order
        page.navigate(baseUrl + "/");
        
        var orderHistoryLink = page.locator("a[href='/order-history.html']");
        orderHistoryLink.click();

        var orderNumberInput = page.locator("[aria-label='Order Number']");
        orderNumberInput.fill(orderNumber);

        var searchButton = page.locator("[aria-label='Search']");
        searchButton.click();

        // Wait for order details to appear
        var orderDetails = page.locator("[role='alert']");
        orderDetails.waitFor(new Locator.WaitForOptions().setTimeout(TestConfiguration.getWaitSeconds() * 1000));

        var orderDetailsText = orderDetails.textContent();

        // Assert - Verify order details heading is displayed
        assertTrue(orderDetailsText.contains("Order Details"), "Should display order details heading");

        // Verify order details in read-only textboxes
        var displayOrderNumber = page.locator("[aria-label='Display Order Number']");
        var displayProductId = page.locator("[aria-label='Display Product ID']");
        var displayQuantity = page.locator("[aria-label='Display Quantity']");
        var displayUnitPrice = page.locator("[aria-label='Display Unit Price']");
        var displayTotalPrice = page.locator("[aria-label='Display Total Price']");

        assertTrue(displayOrderNumber.inputValue().equals(orderNumber), "Should display the order number: " + orderNumber);
        assertTrue(displayProductId.inputValue().equals("11"), "Should display product ID 11");
        assertTrue(displayQuantity.inputValue().equals("3"), "Should display quantity 3");
        assertTrue(displayUnitPrice.inputValue().startsWith("$"), "Should display unit price with $ symbol");
        assertTrue(displayTotalPrice.inputValue().startsWith("$"), "Should display total price with $ symbol");
    }
}