package com.raina.siliconvalleytrail.model;

import com.raina.siliconvalleytrail.util.GameConstants;
import com.raina.siliconvalleytrail.util.GameDisplay;

public class DenverEvent extends AnchoredEvent {

    @Override
    public void execute(GameSession session) {

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║       DENVER STARTUP WORLD CUP       ║");
        System.out.println("║            REGIONAL 🏆                ║");
        System.out.println("╚══════════════════════════════════════╝");

        // 1 in 3 chance they miss the regional
        if (randomChance(3)) {
            System.out.println();
            System.out.println("💔 The regional filled up before your team registered.");
            System.out.println("   You missed it by one hour. The team is gutted.");
            System.out.println("   Inspiration -20.");
            session.setInspiration(Math.max(
                    session.getInspiration() - 20, 0));
            GameDisplay.waitForEnter();

        } else {
            System.out.println();
            System.out.println("🎤 You pitched in front of 200 founders and investors.");
            System.out.println("   The crowd loved the Nebraska underdog story.");
            System.out.println("   Connections +2 | Followers +500!");
            session.setConnections(session.getConnections() + 2);
            session.setFollowers(session.getFollowers() + 500);
            GameDisplay.waitForEnter();
        }

        // restaurant choice regardless of regional outcome
        System.out.println("\n🍽️  The team is starving. Where do you eat in Denver?");
        System.out.println("─────────────────────────────────────");
        System.out.println("1. Casa Bonita — cliff divers, chaos, green chile ($50)");
        System.out.println("2. Elway's — serious steakhouse, serious networking ($500)");
        System.out.println("3. Snooze AM Eatery — legendary pancakes, brutal wait ($150, +1 day)");
        System.out.print("Enter choice (1-3): ");

        int choice = GameDisplay.getPlayerChoice(1, 3);

        switch (choice) {
            case 1 -> handleCasaBonita(session);
            case 2 -> handleElways(session);
            case 3 -> handleSnooze(session);
        }

    }

    private void handleCasaBonita(GameSession session) {
        session.setCash(session.getCash() - 50);
        System.out.println();
        System.out.println("🌮 Casa Bonita is exactly as chaotic as advertised.");

        if (randomChance(2)) {
            // good outcome
            session.setInspiration(Math.min(
                    session.getInspiration() + 15,
                    GameConstants.MAX_INSPIRATION));
            session.setAiTokens(session.getAiTokens() + 3);
            System.out.println("   The cliff divers inspired a wild UI animation idea.");
            System.out.println("   Inspiration +15 | AI Tokens +3 | Cash -$50");
        } else {
            // bad outcome
            session.setInspiration(Math.max(
                    session.getInspiration() - 5, 0));
            System.out.println("   The noise gave everyone a splitting headache.");
            System.out.println("   Worth it for the story though.");
            System.out.println("   Inspiration -5 | Cash -$50");
        }
    }

    private void handleElways(GameSession session) {
        session.setCash(session.getCash() - 500);
        session.setConnections(session.getConnections() + 1);
        System.out.println();
        System.out.println("🥩 Elway's is everything Denver thinks it is.");
        System.out.println("   You networked over a perfect ribeye.");
        System.out.println("   Connections +1 | Cash -$500");

        // danger zone check after expensive meal
        if (session.getCash() < GameConstants.DANGER_ZONE_CASH_THRESHOLD) {
            session.incrementDangerZoneCount();
            System.out.println("   ⚠️  Your runway is getting thin...");
        }
    }

    private void handleSnooze(GameSession session) {
        session.setCash(session.getCash() - 150);
        session.setDaysElapsed(session.getDaysElapsed() + 1);
        session.setInspiration(Math.min(
                session.getInspiration() + 20,
                GameConstants.MAX_INSPIRATION));
        System.out.println();
        System.out.println("☕ The wait was 90 minutes. Worth every second.");
        System.out.println("   Best breakfast your team has ever eaten.");
        System.out.println("   Inspiration +20 | Cash -$150 | +1 day");
    }


}
