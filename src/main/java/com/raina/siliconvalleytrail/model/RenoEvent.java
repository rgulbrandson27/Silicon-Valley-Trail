package com.raina.siliconvalleytrail.model;

import com.raina.siliconvalleytrail.service.CardApiService;
import com.raina.siliconvalleytrail.util.GameConstants;
import com.raina.siliconvalleytrail.util.GameDisplay;

public class RenoEvent extends AnchoredEvent {

    private final CardApiService cardApiService;

    public RenoEvent() {
        this.cardApiService = new CardApiService();
    }

    @Override
    public void execute(GameSession session) {

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║     THE BIGGEST LITTLE CITY 🎰        ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
        System.out.println("Reno. The casino floor glitters.");
        System.out.println("Your engineers exchange nervous glances.");
        System.out.println("The founder smiles.");
        System.out.println();
        System.out.println("1. Try your luck at the tables");
        System.out.println("2. Skip the casino — you need that cash");
        System.out.print("Enter choice (1-2): ");

        int choice = GameDisplay.getPlayerChoice(1, 2);

        if (choice == 1) {
            handleCasino(session);
        } else {
            System.out.println();
            System.out.println("💼 Smart move. The house always wins... usually.");
            System.out.println("   The team grabs coffee and hits the road.");
        }


        GameDisplay.waitForEnter();
    }

    private void handleCasino(GameSession session) {
        System.out.println();
        System.out.println("🃏 The dealer shuffles. You place your bet.");
        System.out.println("   Drawing from the deck...");
        System.out.println();

        String card = cardApiService.drawCard();
        int cashOutcome = cardApiService.getCashOutcome(card);
        String message = cardApiService.getOutcomeMessage(card, cashOutcome);

        System.out.println(message);
        session.setCash(session.getCash() + cashOutcome);

        session.setFollowers(session.getFollowers() + 100);
        System.out.println("   📱 You posted about it. Followers +100 regardless.");

        if (cashOutcome > 0) {
            System.out.println("   ✨ The team is riding high. Inspiration +10!");
            session.setInspiration(Math.min(
                    session.getInspiration() + 10,
                    GameConstants.MAX_INSPIRATION));
        }

        if (cashOutcome < 0) {
            if (session.getCash() < GameConstants.DANGER_ZONE_CASH_THRESHOLD) {
                session.incrementDangerZoneCount();
                System.out.println("   ⚠️  Cash is dangerously low...");
            }
        }
    }
}