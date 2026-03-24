package com.raina.siliconvalleytrail.service;
import com.raina.siliconvalleytrail.model.CompetitionResult;

import com.raina.siliconvalleytrail.model.GameSession;

public class ScoringService {

    // ══════════════════════════════════════════════════════════════
    //  FINAL SCORE BREAKDOWN
    //  Cash Management    = 50 points max
    //  Connections        = 25 points max (5 points each)
    //  Followers Growth   = 25 points max
    //  TOTAL              = 100 points max
    // ══════════════════════════════════════════════════════════════

    public int calculateFinalScore(GameSession session) {
        int cashScore        = calculateCashScore(session);
        int connectionsScore = calculateConnectionsScore(session);
        int followersScore   = calculateFollowersScore(session);
        int total = cashScore + connectionsScore + followersScore;

        displayScoreBreakdown(cashScore, connectionsScore, followersScore, total, session);
        displayCompetitionResult(session, total);  // ← add this

        return total;
    }

    // ── CASH SCORE (50 points max) ────────────────────────────────
    // Compares ending cash to starting cash ($10,000)
    //
    //  Tripled or more  (≥ $30,000)  → 50 points
    //  Doubled          (≥ $20,000)  → 45 points
    //  Same             (= $10,000)  → 40 points
    //  $5,000 - $9,999              → 35 points
    //  $2,000 - $4,999              → 30 points
    //  $1    - $1,999               → 25 points
    //  $0 or less                   →  0 points (should be game over)
    // ─────────────────────────────────────────────────────────────
    private int calculateCashScore(GameSession session) {
        int startingCash = session.getStartingCash();   // $10,000
        int endingCash   = session.getCash();

        if (endingCash >= startingCash * 3)  return 50;  // tripled
        if (endingCash >= startingCash * 2)  return 45;  // doubled
        if (endingCash >= startingCash)      return 40;  // same or better
        if (endingCash >= 5000)              return 35;
        if (endingCash >= 2000)              return 30;
        if (endingCash >= 1)                 return 25;
        return 0;
    }

    // ── CONNECTIONS SCORE (25 points max) ────────────────────────
    // Each connection earned = 5 points
    // Starting connections = 0, so all connections are earned
    //
    //  5+ connections  → 25 points (max)
    //  4 connections   → 20 points
    //  3 connections   → 15 points
    //  2 connections   → 10 points
    //  1 connection    →  5 points
    //  0 connections   →  0 points
    // ─────────────────────────────────────────────────────────────
    private int calculateConnectionsScore(GameSession session) {
        int connections = session.getConnections();
        int score = connections * 5;
        return Math.min(score, 25);  // cap at 25
    }

    // ── FOLLOWERS SCORE (25 points max) ──────────────────────────
    // Compares ending followers to starting followers (450)
    //
    //  10x growth  (≥ 4,500)   → 25 points
    //  5x growth   (≥ 2,250)   → 20 points
    //  3x growth   (≥ 1,350)   → 15 points
    //  2x growth   (≥ 900)     → 10 points
    //  Any growth  (> 450)     →  5 points
    //  No growth   (= 450)     →  0 points
    // ─────────────────────────────────────────────────────────────
    private int calculateFollowersScore(GameSession session) {
        int startingFollowers = 450;
        int endingFollowers   = session.getFollowers();

        if (endingFollowers >= startingFollowers * 10) return 25;  // 10x
        if (endingFollowers >= startingFollowers * 5)  return 20;  // 5x
        if (endingFollowers >= startingFollowers * 3)  return 15;  // 3x
        if (endingFollowers >= startingFollowers * 2)  return 10;  // 2x
        if (endingFollowers > startingFollowers)       return 5;   // any growth
        return 0;
    }

    // ── SCORE DISPLAY ─────────────────────────────────────────────
    private void displayScoreBreakdown(int cashScore, int connectionsScore,
                                       int followersScore, int total,
                                       GameSession session) {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║           FINAL SCORE CARD           ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
        System.out.printf("💰 Cash Management    %3d / 50 pts%n", cashScore);
        System.out.printf("   Started: $%,d → Ended: $%,d%n",
                session.getStartingCash(), session.getCash());
        System.out.println();
        System.out.printf("🔗 Connections        %3d / 25 pts%n", connectionsScore);
        System.out.printf("   Earned %d connection(s) × 5 pts each%n",
                session.getConnections());
        System.out.println();
        System.out.printf("📱 Followers Growth   %3d / 25 pts%n", followersScore);
        System.out.printf("   Started: %,d → Ended: %,d%n",
                450, session.getFollowers());
        System.out.println();
        System.out.println("──────────────────────────────────────");
        System.out.printf("🏆 TOTAL SCORE        %3d / 100 pts%n", total);
        System.out.println("──────────────────────────────────────");
        System.out.println();
        System.out.println(getScoreComment(total));
    }

    private String getScoreComment(int total) {
        if (total >= 90) return "🌟 LEGENDARY! Silicon Valley will never forget you.";
        if (total >= 75) return "🔥 IMPRESSIVE! You've got what it takes.";
        if (total >= 60) return "👍 SOLID RUN! Not bad for a scrappy startup.";
        if (total >= 45) return "😅 YOU SURVIVED. Barely. But you made it.";
        return "📉 Rough journey. The Silicon Prairie believes in second chances.";
    }
    // ── COMPETITION RESULT ────────────────────────────────────────
// Determined by final score
//
//  90-100 pts  → 🥇 1st Place  — $1,000,000 investment
//  75-89 pts   → 🥈 2nd Place  — $250,000 investment
//  60-74 pts   → 🥉 3rd Place  — $100,000 investment
//  below 60    → Did Not Place — better luck next time
// ─────────────────────────────────────────────────────────────
    private void displayCompetitionResult(GameSession session, int total) {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║      STARTUP WORLD CUP RESULTS       ║");
        System.out.println("║    November 18, 2026 — San Francisco ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();

        if (total >= 90) {
            session.setCompetitionResult(CompetitionResult.FIRST);
            System.out.println("🥇 1ST PLACE!");
            System.out.println("   $1,000,000 investment secured!");
            System.out.println("   The Silicon Prairie just put itself on the map.");
        } else if (total >= 75) {
            session.setCompetitionResult(CompetitionResult.SECOND);
            System.out.println("🥈 2ND PLACE!");
            System.out.println("   $250,000 investment secured!");
            System.out.println("   Not bad for a team from Nebraska.");
        } else if (total >= 60) {
            session.setCompetitionResult(CompetitionResult.THIRD);
            System.out.println("🥉 3RD PLACE!");
            System.out.println("   $100,000 investment secured!");
            System.out.println("   You proved the doubters wrong.");
        } else {
            session.setCompetitionResult(CompetitionResult.DID_NOT_PLACE);
            System.out.println("📋 DID NOT PLACE");
            System.out.println("   You made it to San Francisco.");
            System.out.println("   That counts for more than you know.");
        }
    }
}