package com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;

public class HomePage extends BasePage {
    public HomePage(Page page, String baseUrl) {
        super(page, baseUrl);
    }

    public Response navigateTo() {
        return page.navigate(getBaseUrl());
    }

    public String getContent() {
        return page.content();
    }
}
