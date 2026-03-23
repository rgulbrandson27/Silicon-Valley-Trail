package com.raina.siliconvalleytrail.service;

public class GameService {

    // master losing condition checker - call this every day/turn
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

        if (isOutOfEnergy(session)) {
            displayLoss(session, "The team ran out of energy. " +
                    "No protein bars, no coffee, no fuel. " +
                    "Everyone went home.");
            return true;
        }

        if (hasInspirationStreak(session, GameConstants.INSPIRATION_STREAK_LIMIT)) {
            if (session.getInspiration() < GameConstants.MIN_INSPIRATION) {
                displayLoss(session, "The team lost all motivation. " +
                        "3 days of rock bottom morale ended the journey.");
            } else {
                displayLoss(session, "The team burned out completely. " +
                        "3 days of manic energy crashed everything.");
            }
            return true;
        }

        return false;  // no losing condition triggered
    }

    // ─── individual losing condition checks ───────────────────────

    private boolean isOutOfMoney(GameSession session) {
        return session.getCash() <= 0;
    }

    private boolean isTooLate(GameSession session) {
        return session.getDaysRemaining() <= 0
                && !session.getCurrentStop().equals("SanFrancisco");
    }

    private boolean isOutOfEnergy(GameSession session) {
        return checkStreakCondition(session.getEnergyStreak(),
                GameConstants.ENERGY_STREAK_LIMIT);
    }

    private boolean hasInspirationStreak(GameSession session, int streakLimit) {
        return checkStreakCondition(session.getLowInspirationStreak(), streakLimit)
                || checkStreakCondition(session.getHighInspirationStreak(), streakLimit);
    }





    // ─── your reusable day counter ────────────────────────────────
    // streakLimit is configurable - pass 1 for immediate, 3 for gradual

    private boolean checkStreakCondition(int currentStreak, int streakLimit) {
        return currentStreak >= streakLimit;
    }




    // ─── helper ───────────────────────────────────────────────────

    private void displayLoss(GameSession session, String message) {
        session.setGameOver(true);
        GameDisplay.displayGameOver(message, session);
    }
}
