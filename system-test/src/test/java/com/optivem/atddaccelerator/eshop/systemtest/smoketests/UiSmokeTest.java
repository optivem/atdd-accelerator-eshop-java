package com.optivem.atddaccelerator.eshop.systemtest.smoketests;

import com.microsoft.playwright.*;
import com.optivem.atddaccelerator.eshop.systemtest.TestConfiguration;
import com.optivem.atddaccelerator.eshop.systemtest.core.clients.ui.UiClient;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UiSmokeTest {

    private UiClient uiClient;

    @BeforeEach
    void setUp() {
        this.uiClient = new UiClient(TestConfiguration.getBaseUrl());
    }

    @AfterEach
    void tearDown() throws Exception {
        if (uiClient != null) {
            uiClient.close();
        }
    }

    @Test
    void home_shouldReturnHtmlContent() {
        // Navigate and get response
        var homePage = uiClient.getHomePage();
        var response = homePage.navigateTo();

        // Assert
        assertEquals(200, response.status());

        // Check content type is HTML
        var contentType = response.headers().get("content-type");
        assertTrue(contentType != null && contentType.contains("text/html"),
                "Content-Type should be text/html, but was: " + contentType);

        // Check HTML structure using Playwright's content method
        var pageContent = homePage.getContent();
        assertTrue(pageContent.contains("<html"), "Response should contain HTML opening tag");
        assertTrue(pageContent.contains("</html>"), "Response should contain HTML closing tag");
    }
}
