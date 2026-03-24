package com.raina.siliconvalleytrail.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardApiServiceTest {

    private CardApiService cardApiService;

    private static final String[] VALID_VALUES = {
            "ACE", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "JACK", "QUEEN", "KING"
    };

    @BeforeEach
    void setUp() {
        cardApiService = new CardApiService();
    }

    // ── live API tests ────────────────────────────────────────────

    @Test
    void drawCard_shouldReturnValidCardValue() {
        String card = cardApiService.drawCard();
        assertNotNull(card, "Card should not be null");
        assertTrue(isValidCard(card),
                "Card value should be valid but was: " + card);
    }

    // ── mock fallback tests ───────────────────────────────────────

    @Test
    void getMockCard_shouldReturnValidCardValue() {
        for (int i = 0; i < 20; i++) {
            String card = cardApiService.getMockCard();
            assertTrue(isValidCard(card),
                    "Mock card should be valid but was: " + card);
        }
    }

    // ── cash outcome tests ────────────────────────────────────────

    @Test
    void getCashOutcome_ace_shouldReturnJackpot() {
        assertEquals(5000, cardApiService.getCashOutcome("ACE"));
    }

    @Test
    void getCashOutcome_faceCards_shouldReturnSmallWin() {
        assertEquals(1000, cardApiService.getCashOutcome("KING"));
        assertEquals(1000, cardApiService.getCashOutcome("QUEEN"));
        assertEquals(1000, cardApiService.getCashOutcome("JACK"));
    }

    @Test
    void getCashOutcome_midCards_shouldReturnZero() {
        assertEquals(0, cardApiService.getCashOutcome("10"));
        assertEquals(0, cardApiService.getCashOutcome("7"));
    }

    @Test
    void getCashOutcome_lowCards_shouldReturnLoss() {
        assertEquals(-1000, cardApiService.getCashOutcome("6"));
        assertEquals(-2000, cardApiService.getCashOutcome("2"));
    }

    // ── outcome message tests ─────────────────────────────────────

    @Test
    void getOutcomeMessage_win_shouldContainPlus() {
        String message = cardApiService.getOutcomeMessage("ACE", 5000);
        assertTrue(message.contains("+"),
                "Win message should contain '+'");
        assertTrue(message.contains("ACE"),
                "Message should contain card value");
    }

    @Test
    void getOutcomeMessage_loss_shouldContainMinus() {
        String message = cardApiService.getOutcomeMessage("2", -2000);
        assertTrue(message.contains("-"),
                "Loss message should contain '-'");
    }

    @Test
    void getOutcomeMessage_breakEven_shouldContainEven() {
        String message = cardApiService.getOutcomeMessage("7", 0);
        assertTrue(message.contains("even"),
                "Break even message should contain 'even'");
    }

    // ── helper ────────────────────────────────────────────────────

    private boolean isValidCard(String value) {
        for (String valid : VALID_VALUES) {
            if (valid.equals(value)) return true;
        }
        return false;
    }
}