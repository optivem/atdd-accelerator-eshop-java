package com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages.HomePage;

public class UiClient implements AutoCloseable {

    private String baseUrl;
    private Playwright playwright;
    private Browser browser;
    private Page page;

    private HomePage homePage;

    public UiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.playwright = Playwright.create();
        this.browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        this.page = browser.newPage();

        this.homePage = new HomePage(page, baseUrl);
    }

    public HomePage getHomePage() {
        return homePage;
    }

    @Override
    public void close() throws Exception {
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
}
