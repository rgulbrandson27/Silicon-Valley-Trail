package com.raina.siliconvalleytrail.service.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StockApiService {

    private static final String BASE_URL = "https://api.twelvedata.com/time_series";
    private static final String SYMBOL = "KO";
    private static final double MOCK_PRICE = 75.00;  // fallback price

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public StockApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.apiKey = System.getenv("TWELVE_DATA_API_KEY");
    }

    // returns latest KO closing price
    public double getKoClosePrice() {
        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("⚠️  No API key found — using mock KO price.");
            return MOCK_PRICE;
        }
        try {
            String url = BASE_URL + "?apikey=" + apiKey
                    + "&symbol=" + SYMBOL
                    + "&interval=1min"
                    + "&outputsize=1"
                    + "&format=JSON"
                    + "&type=stock";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString());

            JsonNode json = objectMapper.readTree(response.body());
            JsonNode values = json.get("values");

            if (values != null && values.isArray() && !values.isEmpty()) {
                String closeStr = values.get(0).get("close").asText();
                return Double.parseDouble(closeStr);
            }

            System.out.println("⚠️  Unexpected API response — using mock KO price.");
            return MOCK_PRICE;

        } catch (Exception e) {
            System.out.println("⚠️  KO stock API unavailable — using mock price.");
            return MOCK_PRICE;
        }
    }

    // translates KO price into a ration cost multiplier
    // KO above 76 → market confident → cheaper rations (0.9x)
    // KO between 73-76 → neutral → normal rations (1.0x)
    // KO below 73 → market nervous → expensive rations (1.2x)
    public double getRationCostMultiplier(double koPrice) {
        if (koPrice > 76.0) {
            return 0.9;
        } else if (koPrice < 73.0) {
            return 1.2;
        } else {
            return 1.0;
        }
    }

    // display message explaining the market effect
    public String getMarketMessage(double koPrice, double multiplier) {
        if (multiplier < 1.0) {
            return String.format(
                    "📈 KO is trading at $%.4f — market confidence is high. " +
                            "Rations cost 10%% less today.", koPrice);
        } else if (multiplier > 1.0) {
            return String.format(
                    "📉 KO is trading at $%.4f — markets are nervous. " +
                            "Rations cost 20%% more today.", koPrice);
        } else {
            return String.format(
                    "📊 KO is trading at $%.4f — markets are steady. " +
                            "Normal ration costs today.", koPrice);
        }
    }
}
