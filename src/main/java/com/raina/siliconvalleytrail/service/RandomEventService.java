package com.raina.siliconvalleytrail.service;

import com.raina.siliconvalleytrail.model.GameSession;
import com.raina.siliconvalleytrail.model.LearningCurve;
import com.raina.siliconvalleytrail.util.GameConstants;

import java.util.Random;

public class RandomEventService {

    private final Random random;

    public RandomEventService() {
        this.random = new Random();
    }

    // call this each turn — 1 in 3 chance of event firing
    public boolean maybeFireEvent(GameSession session) {
        if (random.nextInt(3) != 0) return false;

        int eventIndex = random.nextInt(11);
        switch (eventIndex) {
            case 0 -> flatTire(session);
            case 1 -> viralPost(session);
            case 2 -> teamConflictTabs(session);
            case 3 -> unexpectedInvestor(session);
            case 4 -> laptopDied(session);
            case 5 -> goodCoffee(session);
            case 6 -> musicConflict(session);
            case 7 -> noInternet(session);
            case 8 -> spoiledFood(session);
            case 9 -> learningCurveEscalates(session);
            case 10 -> forgotWallet(session);
        }
        return true;
    }

    private void flatTire(GameSession session) {
        System.out.println("\n🚗 FLAT TIRE!");
        System.out.println("   The team loses half a day fixing it on the side of I-80.");
        System.out.println("   Cash -$250 | Days +1");
        session.setCash(session.getCash() - 250);
        session.setDaysElapsed(session.getDaysElapsed() + 1);
    }

    private void viralPost(GameSession session) {
        System.out.println("\n📱 YOUR POST WENT VIRAL!");
        System.out.println("   Someone shared your road trip story.");
        System.out.println("   Followers +300 | Inspiration +10");
        session.setFollowers(session.getFollowers() + 300);
        session.setInspiration(Math.min(
                session.getInspiration() + 10,
                GameConstants.MAX_INSPIRATION));
    }

    private void teamConflictTabs(GameSession session) {
        System.out.println("\n😤 TEAM CONFLICT — TABS VS SPACES!");
        System.out.println("   It got heated. Nobody is speaking.");
        System.out.println("   Inspiration -15");
        session.setInspiration(Math.max(
                session.getInspiration() - 15, 0));
    }

    private void unexpectedInvestor(GameSession session) {
        System.out.println("\n💰 UNEXPECTED INVESTOR!");
        System.out.println("   Someone at a rest stop recognized your pitch deck.");
        System.out.println("   Cash +$2000 | Connections +1");
        session.setCash(session.getCash() + 2000);
        session.setConnections(session.getConnections() + 1);
    }

    private void laptopDied(GameSession session) {
        System.out.println("\n💻 LAPTOP DIED!");
        System.out.println("   Engineer 2's laptop gave up the ghost.");
        System.out.println("   Cash -$800 | AI Tokens -3");
        session.setCash(session.getCash() - 800);
        session.setAiTokens(Math.max(session.getAiTokens() - 3, 0));
    }

    private void goodCoffee(GameSession session) {
        System.out.println("\n☕ AMAZING ROADSIDE COFFEE!");
        System.out.println("   A tiny gas station had the best espresso anyone had ever tasted.");
        System.out.println("   Inspiration +15 | Rations +1");
        session.setInspiration(Math.min(
                session.getInspiration() + 15,
                GameConstants.MAX_INSPIRATION));
        session.setRations(session.getRations() + 1);
    }

    private void musicConflict(GameSession session) {
        System.out.println("\n🎵 MUSIC CONFLICT!");
        System.out.println("   The founder wants lo-fi beats. The engineers want death metal.");
        System.out.println("   Nobody wins. The team stops at Best Buy for noise cancelling headphones.");
        System.out.println("   Cash -$400 | Inspiration -5");
        session.setCash(session.getCash() - 400);
        session.setInspiration(Math.max(
                session.getInspiration() - 5, 0));
    }

    private void noInternet(GameSession session) {
        System.out.println("\n📡 NO INTERNET!");
        System.out.println("   Dead zone. The team loses a day waiting for signal.");
        System.out.println("   AI Tokens -2 | Days +1");
        session.setAiTokens(Math.max(session.getAiTokens() - 2, 0));
        session.setDaysElapsed(session.getDaysElapsed() + 1);
    }

    private void spoiledFood(GameSession session) {
        System.out.println("\n🧊 SOMEONE LEFT THE COOLER OPEN!");
        System.out.println("   Half the team's food supply spoiled overnight.");
        System.out.println("   Rations -2");
        session.setRations(Math.max(session.getRations() - 2, 0));
    }

    private void learningCurveEscalates(GameSession session) {
        System.out.println("\n📈 LEARNING CURVE ESCALATES!");

        if (session.getLearningCurve() == LearningCurve.STEADY) {
            session.setLearningCurve(LearningCurve.HIGH);
            System.out.println("   The codebase is getting complex. Things are getting harder.");
            System.out.println("   Learning Curve: STEADY → HIGH");
            System.out.println("   AI Tokens will cost more going forward.");
        } else if (session.getLearningCurve() == LearningCurve.HIGH) {
            session.setLearningCurve(LearningCurve.STEEP);
            System.out.println("   The technical debt is overwhelming. Everything takes longer.");
            System.out.println("   Learning Curve: HIGH → STEEP");
            System.out.println("   AI Tokens cost significantly more going forward.");
        } else {
            // already STEEP — can't get worse, give them a small consolation
            System.out.println("   The team is already deep in the weeds.");
            System.out.println("   But they're getting tougher. Inspiration +5.");
            session.setInspiration(Math.min(
                    session.getInspiration() + 5,
                    GameConstants.MAX_INSPIRATION));
        }
    }

    private void forgotWallet(GameSession session) {
        System.out.println("\n👛 SOMEONE LEFT THEIR WALLET AT THE GAS STATION!");
        System.out.println("   The team had to backtrack 25 miles each way.");
        System.out.println("   Cash -$150 (extra gas) | Days +1");
        session.setCash(session.getCash() - 150);
        session.setDaysElapsed(session.getDaysElapsed() + 1);
        session.setMilesRemaining(session.getMilesRemaining() + 50); // backtrack adds miles
    }
}