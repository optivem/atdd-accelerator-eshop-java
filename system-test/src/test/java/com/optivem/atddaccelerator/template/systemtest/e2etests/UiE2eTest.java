package com.optivem.atddaccelerator.template.systemtest.e2etests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

class UiE2eTest {

    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_WAIT_SECONDS = 10;
    
    private Playwright playwright;
    private Browser browser;
    private Page page;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        page = browser.newPage();
        baseUrl = "http://localhost:" + DEFAULT_PORT;
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

        Locator skuInput = page.locator("[aria-label='SKU']");
        skuInput.fill("ABC1001");

        Locator quantityInput = page.locator("[aria-label='Quantity']");
        quantityInput.fill("5");

        Locator placeOrderButton = page.locator("[aria-label='Place Order']");
        placeOrderButton.click();

        // Wait for confirmation message to appear
        Locator confirmationMessage = page.locator("[role='alert']");
        confirmationMessage.waitFor(new Locator.WaitForOptions().setTimeout(DEFAULT_WAIT_SECONDS * 1000));

        String confirmationMessageText = confirmationMessage.textContent();

        Pattern pattern = Pattern.compile("Success! Order has been created with Order Number ([\\w-]+) and Total Price \\$(\\d+(?:\\.\\d{2})?)");
        var matcher = pattern.matcher(confirmationMessageText);

        assertTrue(matcher.find(), "Confirmation message should match expected pattern. Actual: " + confirmationMessageText);

        String totalPriceString = matcher.group(2);
        double totalPrice = Double.parseDouble(totalPriceString);
        assertTrue(totalPrice > 0, "Total price should be positive. Actual: " + totalPrice);
    }
}