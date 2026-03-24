package com.raina.siliconvalleytrail;

import com.raina.siliconvalleytrail.data.LandmarkData;
import com.raina.siliconvalleytrail.model.*;
import com.raina.siliconvalleytrail.service.*;
import com.raina.siliconvalleytrail.util.GameConstants;
import com.raina.siliconvalleytrail.util.GameDisplay;
import com.raina.siliconvalleytrail.model.GameSession;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        // display intro
        GameDisplay.displayWelcomeMessage();
        GameDisplay.displayGameSummary();
        GameDisplay.displayInstructionsPage2();
        GameDisplay.displayInstructionsPage3();

        // main menu loop
        boolean running = true;
        while (running) {
            System.out.printf("%n══════════════════════════════════════%n");
            System.out.printf("           MAIN MENU%n");
            System.out.printf("══════════════════════════════════════%n");
            System.out.printf("1. New Game%n");
            System.out.printf("2. Quit%n");
            System.out.print("Enter choice (1-2): ");

            int menuChoice = GameDisplay.getPlayerChoice(1, 2);
            switch (menuChoice) {
                case 1 -> runNewGame();
                case 2 -> {
                    System.out.printf("Thanks for playing Silicon Valley Trail!%n");
                    running = false;
                }
            }
        }
    }

    private static void runNewGame() {

        // name this save
        System.out.printf("%n── NAME YOUR SAVE ───────────────────%n");
        String sessionName = GameDisplay.getValidatedName(
                "Name this save (e.g. 'easy run' or 'November 10 attempt')", 100);

        // collect team names// collect team names — 20 char limit
        System.out.printf("%n── BUILD YOUR TEAM ──────────────────%n");
        String founderName = GameDisplay.getValidatedName("Founder name", 20);
        String eng1 = GameDisplay.getValidatedName("Engineer 1 name", 20);
        String eng2 = GameDisplay.getValidatedName("Engineer 2 name", 20);
        String eng3 = GameDisplay.getValidatedName("Engineer 3 name", 20);

        // choose departure date
        System.out.printf("%n── CHOOSE DEPARTURE DATE ────────────%n");
        System.out.printf("1. October 28  (21 days — easy)%n");
        System.out.printf("2. November 1  (17 days — moderate)%n");
        System.out.printf("3. November 5  (13 days — hard)%n");
        System.out.printf("4. November 10 (8 days  — brutal)%n");
        System.out.print("Enter choice (1-4): ");

        int dateChoice = GameDisplay.getPlayerChoice(1, 4);
        DepartureDate departureDate;
        int totalDays;

        switch (dateChoice) {
            case 1 -> { departureDate = DepartureDate.OCTOBER_28; totalDays = 21; }
            case 2 -> { departureDate = DepartureDate.NOVEMBER_1; totalDays = 17; }
            case 3 -> { departureDate = DepartureDate.NOVEMBER_5; totalDays = 13; }
            default -> { departureDate = DepartureDate.NOVEMBER_10; totalDays = 8; }
        }

        // create session
        GameSession session = new GameSession(
                sessionName, founderName, eng1, eng2, eng3,
                departureDate, totalDays,
                GameConstants.STARTING_CASH
        );
        session.setCurrentLandmark("Lincoln");
        session.addLandmarkVisited("Lincoln");

        // load landmarks
        Map<String, Landmark> landmarks = LandmarkData.getLandmarks();

        RouteService routeService = new RouteService();
        GameService gameService = new GameService();
        StockApiService stockApi = new StockApiService();
        RandomEventService randomEventService = new RandomEventService();

        System.out.printf("%nGood luck, %s! The Silicon Prairie believes in you.%n",
                founderName);
        GameDisplay.waitForEnter();

        double koPrice1 = stockApi.getKoClosePrice();
        session.setInitialKoPrice(koPrice1);
        session.setRationCostMultiplier(stockApi.getRationCostMultiplier(koPrice1));
        System.out.println(stockApi.getMarketMessage(koPrice1, session.getRationCostMultiplier()));
        GameDisplay.waitForEnter();

        // ── GAME LOOP ──────────────────────────────────────────────
        while (!session.isGameOver() && !session.hasArrived()) {

            // display current status
            GameDisplay.displayStatus(session);

            // show action menu
            GameDisplay.displayActionMenu();
            int action = GameDisplay.getPlayerChoice(1, 6);

            switch (action) {

                case 1 -> {
                    // travel to next landmark
                    Landmark current = landmarks.get(session.getCurrentLandmark());
                    List<String> nextOptions = current.getNextLandmarkNames();

                    String chosenNext;
                    if (nextOptions.size() == 1) {
                        chosenNext = nextOptions.get(0);
                    } else {
                        // fork point — present choice
                        GameDisplay.displayForkChoice(nextOptions.get(0), nextOptions.get(1));
                        int forkChoice = GameDisplay.getPlayerChoice(1, 2);
                        chosenNext = nextOptions.get(forkChoice - 1);
                    }

                    Landmark next = landmarks.get(chosenNext);
                    routeService.travel(session, next);
                    session.setCurrentLandmark(chosenNext);
                    session.addLandmarkVisited(chosenNext);
                    GameDisplay.displayArrival(next.getName(), next.getDescription(), next.getDistanceFromPrevious());

                    // apply landmark gains
                    session.setFollowers(session.getFollowers() + next.getFollowerGain());
                    session.setInspiration(Math.min(
                            session.getInspiration() + next.getInspirationGain(),
                            GameConstants.MAX_INSPIRATION));
                    // landmark specific events — fire on arrival
                    if (chosenNext.equals("Reno"))   new RenoEvent().execute(session);
                    // landmark specific events — fire on arrival
                    if (chosenNext.equals("Pikes Peak")) {
                        double koPrice2 = stockApi.getKoClosePrice();
                        session.setRationCostMultiplier(stockApi.getRationCostMultiplier(koPrice2));

                        // compare to initial price
                        double change = koPrice2 - session.getInitialKoPrice();
                        if (change > 0) {
                            System.out.printf("📈 KO is up $%.4f since Lincoln. Market confidence growing.%n", change);
                        } else if (change < 0) {
                            System.out.printf("📉 KO is down $%.4f since Lincoln. Markets getting nervous.%n", Math.abs(change));
                        } else {
                            System.out.println("📊 KO hasn't moved since Lincoln. Markets holding steady.");
                        }
                        System.out.println(stockApi.getMarketMessage(koPrice2, session.getRationCostMultiplier()));
                        GameDisplay.waitForEnter();
                    }

// Call 3 — market check at Santa Clara
                    if (chosenNext.equals("Santa Clara School of Law")) {
                        double koPrice3 = stockApi.getKoClosePrice();
                        session.setRationCostMultiplier(stockApi.getRationCostMultiplier(koPrice3));
                        System.out.println(stockApi.getMarketMessage(koPrice3, session.getRationCostMultiplier()));
                    }

                    // check if arrived at San Francisco
                    if (chosenNext.equals("San Francisco")) {
                        session.setHasArrived(true);
                        GameDisplay.displayVictory(session);
                    }
                }

                case 2 -> {
                    // rest — restore inspiration, use a ration
                    session.setRations(session.getRations() - 1);
                    session.setInspiration(Math.min(
                            session.getInspiration() + 10,
                            GameConstants.MAX_INSPIRATION));
                    session.setDaysElapsed(session.getDaysElapsed() + 1);
                    System.out.printf("%nThe team rests. Inspiration +10, Rations -1.%n");
                }

                case 3 -> {
                    // work on product — gain AI tokens, costs cash
                    session.setAiTokens(session.getAiTokens() + 5);
                    session.setCash(session.getCash() - 500);
                    session.setDaysElapsed(session.getDaysElapsed() + 1);
                    System.out.printf("%nThe team grinds. AI Tokens +5, Cash -$500.%n");
                }

                case 4 -> {
                    // network — gain connections, costs cash
                    session.setConnections(session.getConnections() + 1);
                    session.setCash(session.getCash() - 300);
                    session.setDaysElapsed(session.getDaysElapsed() + 1);
                    System.out.printf("%nYou worked the room. Connections +1, Cash -$300.%n");
                }

                case 5 -> {
                    // TODO: save game (Jackson serialization)
                    System.out.printf("%nSave not yet implemented. Coming soon!%n");
                }

                case 6 -> {
                    System.out.printf("%nReturning to main menu...%n");
                    session.setGameOver(true);
                }
            }

            // random event
            boolean eventFired = randomEventService.maybeFireEvent(session);
            if (eventFired) {
                GameDisplay.waitForEnter();
            }
            // update inspiration streaks
            updateInspirationStreaks(session);

            // check losing conditions
            gameService.checkDangerZone(session);
            gameService.checkLosingConditions(session);
        }
    }

    private static void updateInspirationStreaks(GameSession session) {
        int inspiration = session.getInspiration();

        if (inspiration < GameConstants.MIN_INSPIRATION) {
            session.setLowInspirationStreak(session.getLowInspirationStreak() + 1);
            session.setHighInspirationStreak(0);
        } else if (inspiration > GameConstants.MAX_INSPIRATION) {
            session.setHighInspirationStreak(session.getHighInspirationStreak() + 1);
            session.setLowInspirationStreak(0);
        } else {
            session.setLowInspirationStreak(0);
            session.setHighInspirationStreak(0);
        }
    }
}

