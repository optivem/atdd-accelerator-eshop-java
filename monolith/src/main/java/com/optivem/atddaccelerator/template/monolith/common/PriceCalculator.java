package com.optivem.atddaccelerator.template.monolith.common;

public class PriceCalculator {
    public static double calculatePrice(String sku, int quantity) {
        var price = getPrice(sku);
        return price * quantity;
    }

    private static double getPrice(String sku) {
        // TODO: VJ: Replace with actual ERP integration
        // For testing purposes, use hardcoded prices
        return switch (sku) {
            case "ABC", "ABC1001" -> 2.50;
            case "DEF" -> 5.00;
            case "GHI" -> 10.00;
            default -> 1.00;
        };
        
        /* Original ERP integration code - uncomment when ERP is available
        try {
            var erpUrl = System.getenv("ERP_URL");
            System.out.println("Going to contact: " + erpUrl);

            var url = erpUrl + "/products/" + sku;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.body());
            return node.get("price").asDouble();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch price", e);
        }
        */
    }
}
