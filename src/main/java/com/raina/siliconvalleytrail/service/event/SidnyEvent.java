package com.raina.siliconvalleytrail.service.event;

import com.raina.siliconvalleytrail.model.GameSession;

public class SidnyEvent extends AnchoredEvent {

    public void execute(GameSession session) {
        System.out.println();
        System.out.println("SIDNY EVENT");
        System.out.println("─────────────────────────────────────");
        System.out.println("You arrive expecting the original Cabela's headquarters.");
        System.out.println("Instead, you find it has been bought out.");
        System.out.println("After layoffs hit the area, part of the space has been");
        System.out.println("converted into a startup innovation center.");
        System.out.println("Your team spends the evening meeting founders, swapping ideas,");
        System.out.println("and finding unexpected momentum.");
        System.out.println();
        System.out.println("Results:");
        System.out.println("+1 Connection");
        System.out.println("+10 Inspiration");
        System.out.println("-$300 Hotel stay");
        System.out.println("+1 Extra day");
        System.out.println();

        session.setConnections(session.getConnections() + 1);
        session.setInspiration(Math.min(session.getInspiration() + 10, 100));
        session.setCash(session.getCash() - 300);
        session.setDaysElapsed(session.getDaysElapsed() + 1);
    }
}