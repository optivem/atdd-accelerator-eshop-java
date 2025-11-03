package com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui;

import com.microsoft.playwright.*;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages.HomePage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    public HomePage openHomePage() {
        var response = page.navigate(baseUrl);
        assertEquals(200, response.status());

        var contentType = response.headers().get("content-type");
        assertTrue(contentType != null && contentType.contains("text/html"),
                "Content-Type should be text/html, but was: " + contentType);

        // Check HTML structure using Playwright's content method
        var pageContent = page.content();
        assertTrue(pageContent.contains("<html"), "Response should contain HTML opening tag");
        assertTrue(pageContent.contains("</html>"), "Response should contain HTML closing tag");
        return homePage;
    }

    @Override
    public void close() {
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
