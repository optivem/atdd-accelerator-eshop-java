package com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.pages;

import com.microsoft.playwright.Page;

public abstract class BasePage {
    protected final Page page;
    private final String baseUrl;

    public BasePage(Page page, String baseUrl) {
        this.page = page;
        this.baseUrl = baseUrl;
    }

    protected String getBaseUrl() {
        return baseUrl;
    }

    protected String getUrl(String path) {
        return baseUrl + path;
    }
}
