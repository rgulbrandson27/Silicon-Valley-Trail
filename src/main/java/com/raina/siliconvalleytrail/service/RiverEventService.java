package com.raina.siliconvalleytrail.service;

import com.raina.siliconvalleytrail.model.GameSession;
import com.raina.siliconvalleytrail.model.AnchoredEvent;
import com.raina.siliconvalleytrail.util.GameConstants;
import com.raina.siliconvalleytrail.util.GameDisplay;

public class RiverEventService extends AnchoredEvent {

    @Override
    public void execute(GameSession session) {

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║       COLORADO RIVER CROSSING  🏞️    ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
        System.out.println("The Colorado River stretches out before you.");
        System.out.println("The water is fast. The canyon walls are enormous.");
        System.out.println("A rafting guide waves at your team from the bank.");
        System.out.println();
        System.out.println("1. Cross and continue — stay on schedule");
        System.out.println("2. Go white water rafting — YOLO ($500, uses 1 ration day)");
        System.out.print("Enter choice (1-2): ");

        int choice = GameDisplay.getPlayerChoice(1, 2);

        if (choice == 1) {
            handleCrossAndContinue(session);
        } else {
            handleWhiteWaterRafting(session);
        }

        GameDisplay.waitForEnter();
    }

    private void handleCrossAndContinue(GameSession session) {
        System.out.println();
        System.out.println("🚗 The team crosses the bridge and pushes on.");
        System.out.println("   The view from the canyon rim is breathtaking.");
        System.out.println("   Inspiration +10");
        session.setInspiration(Math.min(
                session.getInspiration() + 10,
                GameConstants.MAX_INSPIRATION));
    }

    private void handleWhiteWaterRafting(GameSession session) {
        session.setCash(session.getCash() - 500);
        session.setRations(Math.max(session.getRations() - 1, 0));

        System.out.println();
        System.out.println("🚣 The team gears up and hits the rapids!");
        System.out.println("   This is exactly why you took the road trip.");

        // 1 in 3 chance something goes wrong
        if (randomChance(3)) {
            handleRaftingAccident(session);
        } else {
            handleRaftingSuccess(session);
        }
    }

    private void handleRaftingSuccess(GameSession session) {
        session.setInspiration(Math.min(
                session.getInspiration() + 25,
                GameConstants.MAX_INSPIRATION));
        session.setFollowers(session.getFollowers() + 200);
        System.out.println();
        System.out.println("🌊 WHAT A RIDE!");
        System.out.println("   Everyone made it through the rapids.");
        System.out.println("   Someone posted the GoPro footage.");
        System.out.println("   Inspiration +25 | Followers +200 | Cash -$500 | Rations -1");
    }

    private void handleRaftingAccident(GameSession session) {
        session.setDaysElapsed(session.getDaysElapsed() + 1);
        session.setCash(session.getCash() - 200);
        System.out.println();
        System.out.println("💦 SOMEONE FELL IN!");
        System.out.println("   Everyone is fine but gear got soaked.");
        System.out.println("   The team spent the rest of the day drying out.");
        System.out.println("   Days +1 | Cash -$200 (extra) | Cash -$500 | Rations -1");
        System.out.println("   At least it makes a great story.");
        session.setInspiration(Math.min(
                session.getInspiration() + 10,
                GameConstants.MAX_INSPIRATION));
    }
}