package com.raina.siliconvalleytrail.service;

import com.raina.siliconvalleytrail.model.DepartureDate;
import com.raina.siliconvalleytrail.model.GameSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private GameService gameService;
    private GameSession session;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
        session = new GameSession(
                "test save", "Founder", "Eng1", "Eng2", "Eng3",
                DepartureDate.OCTOBER_28, 21, 10000
        );
    }

    @Test
    void cashHitsZero_shouldTriggerGameOver() {
        session.setCash(0);
        gameService.checkLosingConditions(session);
        assertTrue(session.isGameOver(),
                "Game should be over when cash hits zero");
    }

    @Test
    void cashAboveZero_shouldNotTriggerGameOver() {
        session.setCash(1);
        gameService.checkLosingConditions(session);
        assertFalse(session.isGameOver(),
                "Game should not be over when cash is above zero");
    }

    @Test
    void rationHitsZero_shouldTriggerGameOver() {
        session.setRations(0);
        gameService.checkLosingConditions(session);
        assertTrue(session.isGameOver(),
                "Game should be over when rations hit zero");
    }

    @Test
    void daysRemaining_zero_shouldTriggerGameOver() {
        session.setDaysElapsed(21); // totalDays = 21 so daysRemaining = 0
        gameService.checkLosingConditions(session);
        assertTrue(session.isGameOver(),
                "Game should be over when deadline is missed");
    }
}