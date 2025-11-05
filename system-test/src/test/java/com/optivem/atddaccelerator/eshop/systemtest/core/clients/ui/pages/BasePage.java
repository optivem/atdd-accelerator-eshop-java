package com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;

public abstract class BasePage {
    protected final Page page;
    private final String baseUrl;
    private final double timeoutMilliseconds;

    private static final double DEFAULT_TIMEOUT_MILLISECONDS = TestConfiguration.getWaitSeconds() * 1000;

    public BasePage(Page page, String baseUrl, double timeOutMilliseconds) {
        this.page = page;
        this.baseUrl = baseUrl;
        this.timeoutMilliseconds = timeOutMilliseconds;
    }

    public BasePage(Page page, String baseUrl) {
        this(page, baseUrl, DEFAULT_TIMEOUT_MILLISECONDS);
    }

    protected String getBaseUrl() {
        return baseUrl;
    }

    private Locator.WaitForOptions getWaitForOptions() {
        return new Locator.WaitForOptions().setTimeout(timeoutMilliseconds);
    }

    protected void wait(Locator locator) {
        locator.waitFor(getWaitForOptions());
    }

    protected void fill(String selector, String text) {
        var input = page.locator(selector);
        wait(input);
        input.fill(text);
    }

    protected void click(String selector) {
        var button = page.locator(selector);
        wait(button);
        button.click();
    }

    protected String readTextContent(String selector) {
        var locator = page.locator(selector);
        wait(locator);
        return locator.textContent();
    }
}
