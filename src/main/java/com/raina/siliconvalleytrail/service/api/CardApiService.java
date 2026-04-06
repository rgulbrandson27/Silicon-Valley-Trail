package com.raina.siliconvalleytrail.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Random;

public class CardApiService {

    private static final String BASE_URL = "https://deckofcardsapi.com/api/deck";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Random random;

    private record DeckResponse(boolean success, String deck_id) {}

    private record DrawResponse(boolean success, List<CardData> cards) {}

    private record CardData(String value) {}

    public CardApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.random = new Random();
    }

    public String drawCard() {
        try {
            DeckResponse deck = sendRequest(BASE_URL + "/new/shuffle/", DeckResponse.class);
            if (!deck.success()) throw new RuntimeException("Deck request failed");

            DrawResponse draw = sendRequest(BASE_URL + "/" + deck.deck_id() + "/draw/?count=1", DrawResponse.class);
            if (!draw.success() || draw.cards() == null || draw.cards().isEmpty()) {
                throw new RuntimeException("Draw request failed");
            }

            return draw.cards().get(0).value();

        } catch (Exception e) {
            System.out.println("Card API unavailable — drawing from house deck...");
            return getMockCard();
        }
    }

    private <T> T sendRequest(String url, Class<T> responseType) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), responseType);
    }

    // fallback — used when API is unavailable
    public String getMockCard() {
        String[] values = {"ACE", "2", "3", "4", "5", "6", "7",
                "8", "9", "10", "JACK", "QUEEN", "KING"};
        return values[random.nextInt(values.length)];
    }

    // maps card value to cash outcome
    public int getCashOutcome(String cardValue) {
        return switch (cardValue) {
            case "ACE"                   -> 5000;
            case "KING", "QUEEN", "JACK" -> 1000;
            case "10", "9", "8", "7"     -> 0;
            case "6", "5", "4"           -> -1000;
            case "3", "2"                -> -2000;
            default                      -> 0;
        };
    }

    public String getOutcomeMessage(String cardValue, int cashOutcome) {
        String article = (cardValue.equals("ACE") || cardValue.equals("8")) ? "an" : "a";

        if (cashOutcome > 0) {
            return String.format("🃏 You drew %s %s! The table erupts. +$%,d!",
                    article, cardValue, cashOutcome);
        } else if (cashOutcome < 0) {
            return String.format("🃏 You drew %s %s. The house wins. -$%,d.",
                    article, cardValue, Math.abs(cashOutcome));
        } else {
            return String.format("🃏 You drew %s %s. You walk away even. Not bad.",
                    article, cardValue);
        }
    }
}
