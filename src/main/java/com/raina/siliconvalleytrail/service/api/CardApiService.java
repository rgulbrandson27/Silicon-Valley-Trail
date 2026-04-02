package com.raina.siliconvalleytrail.service.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

public class CardApiService {

    private static final String BASE_URL = "https://deckofcardsapi.com/api/deck";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Random random;

    public CardApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.random = new Random();
    }

    // main method — called at Reno
    public String drawCard() {
        try {
            String deckId = getNewDeckId();
            if (deckId == null) return getMockCard();
            return drawFromDeck(deckId);
        } catch (Exception e) {
            System.out.println("Card API unavailable — drawing from house deck...");
            return getMockCard();
        }
    }

    // step 1 — get a new shuffled deck id
    private String getNewDeckId() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/new/shuffle/"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString());

            JsonNode json = objectMapper.readTree(response.body());

            if (json.get("success").asBoolean()) {
                return json.get("deck_id").asText();
            }
            return null;

        } catch (Exception e) {
            return null;
        }
    }

    // step 2 — draw one card from the deck
    private String drawFromDeck(String deckId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + deckId + "/draw/?count=1"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString());

            JsonNode json = objectMapper.readTree(response.body());

            if (json.get("success").asBoolean()) {
                JsonNode card = json.get("cards").get(0);
                return card.get("value").asText();
            }
            return getMockCard();

        } catch (Exception e) {
            return getMockCard();
        }
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

    // display message for each outcome
    public String getOutcomeMessage(String cardValue, int cashOutcome) {
        if (cashOutcome > 0) {
            return String.format("🃏 You drew a %s! The table erupts. +$%,d!",
                    cardValue, cashOutcome);
        } else if (cashOutcome < 0) {
            return String.format("🃏 You drew a %s. The house wins. -$%,d.",
                    cardValue, Math.abs(cashOutcome));
        } else {
            return String.format("🃏 You drew a %s. You walk away even. Not bad.",
                    cardValue);
        }
    }
}
//🃏