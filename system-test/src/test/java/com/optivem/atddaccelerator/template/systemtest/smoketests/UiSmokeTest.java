package com.optivem.atddaccelerator.template.systemtest.smoketests;

import com.microsoft.playwright.*;
import com.optivem.atddaccelerator.template.systemtest.TestConfiguration;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UiSmokeTest {

    @Test
    void home_shouldReturnHtmlContent() {
        try (var playwright = Playwright.create()) {
            var browser = playwright.chromium().launch();
            var page = browser.newPage();
            
            // Navigate and get response
            var response = page.navigate(TestConfiguration.getBaseUrl());
            
            // Assert
            assertEquals(200, response.status());
            
            // Check content type is HTML
            var contentType = response.headers().get("content-type");
            assertTrue(contentType != null && contentType.contains("text/html"), 
                      "Content-Type should be text/html, but was: " + contentType);
            
            // Check HTML structure using Playwright's content method
            var pageContent = page.content();
            assertTrue(pageContent.contains("<html"), "Response should contain HTML opening tag");
            assertTrue(pageContent.contains("</html>"), "Response should contain HTML closing tag");
            
            browser.close();
        }
    }
}
