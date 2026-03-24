package com.raina.siliconvalleytrail.service;

import com.raina.siliconvalleytrail.model.CompetitionResult;
import com.raina.siliconvalleytrail.model.GameSession;
import com.raina.siliconvalleytrail.model.DepartureDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoringServiceTest {

    private ScoringService scoringService;
    private GameSession session;

    @BeforeEach
    void setUp() {
        scoringService = new ScoringService();
        session = new GameSession(
                "test save", "Founder", "Eng1", "Eng2", "Eng3",
                DepartureDate.OCTOBER_28, 21, 10000
        );
    }

    // ── competition placement tests ───────────────────────────────

    @Test
    void score90to100_shouldResultInFirstPlace() {
        // triple the money = 50pts, 5 connections = 25pts, 10x followers = 25pts = 100
        session.setCash(30000);        // tripled → 50 pts
        session.setConnections(5);     // 5 × 5 = 25 pts
        session.setFollowers(4500);    // 10x → 25 pts

        scoringService.calculateFinalScore(session);

        assertEquals(CompetitionResult.FIRST, session.getCompetitionResult());
    }

    @Test
    void score75to89_shouldResultInSecondPlace() {
        // doubled money = 45pts, 4 connections = 20pts, no follower growth = 0pts = 65
        // need 75-89 so: doubled=45, 4 connections=20, 3x followers=15 = 80
        session.setCash(20000);        // doubled → 45 pts
        session.setConnections(4);     // 4 × 5 = 20 pts
        session.setFollowers(1350);    // 3x → 15 pts

        scoringService.calculateFinalScore(session);

        assertEquals(CompetitionResult.SECOND, session.getCompetitionResult());
    }

    @Test
    void score60to74_shouldResultInThirdPlace() {
        // same cash=40, 3 connections=15, any growth=5 = 60
        session.setCash(10000);        // same → 40 pts
        session.setConnections(3);     // 3 × 5 = 15 pts
        session.setFollowers(500);     // any growth → 5 pts

        scoringService.calculateFinalScore(session);

        assertEquals(CompetitionResult.THIRD, session.getCompetitionResult());
    }

    @Test
    void scoreBelow60_shouldResultInDidNotPlace() {
        // $5000=35, 1 connection=5, no growth=0 = 40
        session.setCash(5000);         // → 35 pts
        session.setConnections(1);     // 1 × 5 = 5 pts
        session.setFollowers(450);     // no growth → 0 pts

        scoringService.calculateFinalScore(session);

        assertEquals(CompetitionResult.DID_NOT_PLACE, session.getCompetitionResult());
    }
}