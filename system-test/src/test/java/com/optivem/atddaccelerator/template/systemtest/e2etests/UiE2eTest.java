package com.optivem.atddaccelerator.template.systemtest.e2etests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UiE2eTest {

    @Test
    void fetchTodo_shouldDisplayTodoDataInUI() {
        // DISCLAIMER: This is an example of a badly written test
        // which unfortunately simulates real-life software test projects.
        // This is the starting point for our ATDD Accelerator exercises.

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            
            // Navigate to the todo page
            page.navigate("http://localhost:8080/todos");
            
            // 1. Check there's a textbox with id
            Locator todoIdInput = page.locator("#todoId");
            assertTrue(todoIdInput.isVisible(), "Todo ID input textbox should be visible");
            
            // 2. Input value 4 into that textbox
            todoIdInput.fill("4");
            
            // 3. Click "Fetch Todo" button
            Locator fetchButton = page.locator("#fetchTodo");
            fetchButton.click();
            
            // 4. Wait for the result to appear and contain actual data
            Locator todoResult = page.locator("#todoResult");
            
            // Wait for the result div to become visible first
            todoResult.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            
            // Wait a bit more for the API call to complete and content to load
            page.waitForTimeout(3000);
            
            String resultText = todoResult.textContent();
            
            // Debug: Print the actual result text
            System.out.println("Actual result text: " + resultText);
            
            // Verify the todo data is displayed (more flexible checking)
            assertTrue(resultText.contains("userId") && (resultText.contains("1") || resultText.contains(": 1")), 
                      "Result should contain userId: 1. Actual text: " + resultText);
            assertTrue(resultText.contains("id") && (resultText.contains("4") || resultText.contains(": 4")), 
                      "Result should contain id: 4. Actual text: " + resultText);
            assertTrue(resultText.contains("title"), 
                      "Result should contain title field. Actual text: " + resultText);
            assertTrue(resultText.contains("completed"), 
                      "Result should contain completed field. Actual text: " + resultText);
            
            browser.close();
        }
    }
}