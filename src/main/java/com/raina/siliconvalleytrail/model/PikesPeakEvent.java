package com.raina.siliconvalleytrail.model;

import com.raina.siliconvalleytrail.model.AnchoredEvent;
import com.raina.siliconvalleytrail.model.GameSession;
import com.raina.siliconvalleytrail.util.GameConstants;
import com.raina.siliconvalleytrail.util.GameDisplay;

public class PikesPeakEvent extends AnchoredEvent {

    @Override
    public void execute(GameSession session) {

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         PIKES PEAK  ⛰️               ║");
        System.out.println("║      America's Mountain — 14,115 ft  ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
        System.out.println("The team arrives at the Broadmoor Manitou &");
        System.out.println("Pikes Peak Cog Railway depot in Manitou Springs.");
        System.out.println("The historic red train has been climbing this");
        System.out.println("mountain since 1891.");
        System.out.println();
        System.out.println("You check the schedule. Last train: 4:30pm.");
        System.out.println("Your watch says 4:47pm.");
        System.out.println("You missed it.");
        System.out.println();
        System.out.println("What do you do?");
        System.out.println("─────────────────────────────────────");
        System.out.println("1. Drive up the Pikes Peak Highway yourself");
        System.out.println("   (costs 1 day, but the views are worth it)");
        System.out.println("2. Skip it — you don't have time to waste");
        System.out.printf("Enter choice (1-2): ");

        int choice = GameDisplay.getPlayerChoice(1, 2);

        if (choice == 1) {
            driveToSummit(session);
        } else {
            System.out.println();
            System.out.println("🚗 The team drives past the entrance and keeps heading west.");
            System.out.println("   Someone stares out the window for a long time.");
        }

//        GameDisplay.waitForEnter();
    }

    private void driveToSummit(GameSession session) {
        System.out.println();
        System.out.println("🚗 The team hits the 19-mile Pikes Peak Highway.");
        System.out.println("   153 turns. No guardrails on most of it.");
        System.out.println("   Engineer 3 grips the door handle the entire way.");
        System.out.println();
        System.out.println("   But then — the summit.");
        System.out.println("   14,115 feet. Five states visible on a clear day.");
        System.out.println("   The team stands at the top of America's Mountain");
        System.out.println("   and feels genuinely unstoppable.");
        System.out.println();
        System.out.println("   Followers +500 | Inspiration +20 | Days +1");

        session.setFollowers(session.getFollowers() + 500);
        session.setInspiration(Math.min(
                session.getInspiration() + 20, 100));
        session.setDaysElapsed(session.getDaysElapsed() + 1);

//        GameDisplay.waitForEnter();

        // 1 in 2 chance of dropped phone
        if (randomChance(2)) {
            droppedPhone(session);
        }
    }
    private void droppedPhone(GameSession session) {
        System.out.println("\n📱 OH NO.");
        System.out.println("   Engineer 1 leaned over the summit lookout");
        System.out.println("   for the perfect selfie.");
        System.out.println("   The wind caught the moment.");
        System.out.println("   The phone did not survive.");
        System.out.println();
        System.out.println("   The photo was incredible though.");
        System.out.println("   Someone screenshot it before it went offline.");
        System.out.println("   Followers +200 (RIP to the phone)");
        System.out.println("   Cash -$600 | Followers +200");

        session.setCash(session.getCash() - 600);
        session.setFollowers(session.getFollowers() + 200);
    }
}

