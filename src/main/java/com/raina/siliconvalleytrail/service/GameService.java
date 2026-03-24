package com.raina.siliconvalleytrail.service;

import com.raina.siliconvalleytrail.model.GameSession;
import com.raina.siliconvalleytrail.util.GameConstants;
import com.raina.siliconvalleytrail.util.GameDisplay;

public class GameService {

    // call this every turn after actions resolve
    public boolean checkLosingConditions(GameSession session) {

        if (isOutOfMoney(session)) {
            displayLoss(session, "You ran out of runway. The dream is over... for now.");
            return true;
        }

        if (isTooLate(session)) {
            displayLoss(session, "You missed the Startup World Cup deadline. " +
                    "The finals were on November 18th. So close.");
            return true;
        }

        if (isOutOfRations(session)) {
            displayLoss(session, "The team ran out of food and coffee. " +
                    "No fuel left to keep going. Everyone went home.");
            return true;
        }

        if (hasInspirationStreak(session)) {
            if (session.getInspiration() < GameConstants.LOW_INSPIRATION_THRESHOLD) {
                displayLoss(session, "The team lost all motivation. " +
                        "Too many days of rock bottom inspiration ended the journey.");
            } else {
                displayLoss(session, "The team burned out completely. " +
                        "Too many days of manic energy crashed everything.");
            }
            return true;
        }

        return false;
    }

    // win condition — call after arriving at San Francisco
    public boolean checkWinCondition(GameSession session) {
        return session.hasArrived();
    }

    // individual losing condition checks
    private boolean isOutOfMoney(GameSession session) {
        return session.getCash() <= 0;
    }

    private boolean isTooLate(GameSession session) {
        return session.getDaysRemaining() <= 0 && !session.hasArrived();
    }

    private boolean isOutOfRations(GameSession session) {
        return session.getRations() <= 0;
    }

    // streak triggers on day 3 — they get 2 days to fix it
    private boolean hasInspirationStreak(GameSession session) {
        return session.getLowInspirationStreak() > GameConstants.INSPIRATION_STREAK_LIMIT
                || session.getHighInspirationStreak() > GameConstants.INSPIRATION_STREAK_LIMIT;
    }

    // danger zone tracker — call when cash drops low
    public void checkDangerZone(GameSession session) {
        if (session.getCash() < GameConstants.DANGER_ZONE_CASH_THRESHOLD) {
            session.incrementDangerZoneCount();
        }
    }

    private void displayLoss(GameSession session, String message) {
        session.setGameOver(true);
        GameDisplay.displayGameOver(message, session);
    }
}