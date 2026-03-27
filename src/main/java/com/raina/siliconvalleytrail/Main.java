package com.raina.siliconvalleytrail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raina.siliconvalleytrail.data.LandmarkData;
import com.raina.siliconvalleytrail.model.*;
import com.raina.siliconvalleytrail.service.*;
import com.raina.siliconvalleytrail.util.GameConstants;
import com.raina.siliconvalleytrail.util.GameDisplay;
import com.raina.siliconvalleytrail.model.GameSession;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        GameDisplay.displayWelcomeMessage();
        GameDisplay.displayGameSummary();
        GameDisplay.waitForEnter();
        GameDisplay.displayInstructionsPage2();
        GameDisplay.displayInstructionsPage3();
        GameDisplay.waitForEnter();

        PersistenceService persistenceService = new PersistenceService();
        boolean running = true;
        while (running) {
            System.out.printf("%n══════════════════════════════════════%n");
            System.out.printf("           MAIN MENU%n");
            System.out.printf("══════════════════════════════════════%n");
            System.out.printf("1. New Game%n");
            System.out.printf("2. Load Game%n");
            System.out.printf("3. Quit%n");
            System.out.print("Enter choice (1-3): ");

            int menuChoice = GameDisplay.getPlayerChoice(1, 3);
            switch (menuChoice) {
                case 1 -> runNewGame(persistenceService);
                case 2 -> {
                    List<String> saves = persistenceService.listSaves();
                    if (saves.isEmpty()) {
                        System.out.println("\nNo saved games found.");
                        GameDisplay.waitForEnter();
                    } else {
                        for (int i = 0; i < saves.size(); i++) {
                            System.out.printf("%d. %s%n", i + 1, saves.get(i));
                        }
                        System.out.print("Enter choice: ");
                        int choice = GameDisplay.getPlayerChoice(1, saves.size());
                        GameSession session = persistenceService.loadGame(saves.get(choice - 1));
                        if (session != null) {
                            runGame(session, persistenceService);
                        }
                    }
                }
                case 3 -> {
                    System.out.printf("Thanks for playing Silicon Valley Trail!%n");
                    running = false;
                }
            }
        }
    }

    private static void runNewGame(PersistenceService persistenceService) {

        System.out.printf("%n── WHAT WOULD YOU LIKE TO CALL THIS GAME ───────%n");
        String sessionName = GameDisplay.getValidatedName(
                "Game Name: (e.g. 'easy run' or 'November 10 attempt')", 100);

        System.out.printf("%n── BUILD YOUR TEAM ──────────────────%n");
        String founderName = GameDisplay.getValidatedName("Founder name", 20);
        String eng1 = GameDisplay.getValidatedName("Engineer 1 name", 20);
        String eng2 = GameDisplay.getValidatedName("Engineer 2 name", 20);
        String eng3 = GameDisplay.getValidatedName("Engineer 3 name", 20);

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

        GameSession session = new GameSession(
                sessionName, founderName, eng1, eng2, eng3,
                departureDate, totalDays,
                GameConstants.STARTING_CASH
        );
        session.setCurrentLandmark("Lincoln");
        session.addLandmarkVisited("Lincoln");

        StockApiService stockApi = new StockApiService();
        double koPrice1 = stockApi.getKoClosePrice();
        session.setInitialKoPrice(koPrice1);
        session.setLastKoPrice(koPrice1);
        session.setRationCostMultiplier(stockApi.getRationCostMultiplier(koPrice1));

        System.out.printf("%nGood luck, and safe travels!%n");
        GameDisplay.waitForEnter();
        System.out.println("Starting Overview");
        GameDisplay.displayStatus(session);
        System.out.printf("(Starting Coca-Cola rate: $%.4f)%n", koPrice1);
        GameDisplay.waitForEnter();

        runGame(session, persistenceService);
    }

    private static void runGame(GameSession session, PersistenceService persistenceService) {

        Map<String, Landmark> landmarks = LandmarkData.getLandmarks();
        RouteService routeService = new RouteService();
        GameService gameService = new GameService();
        StockApiService stockApi = new StockApiService();
        RandomEventService randomEventService = new RandomEventService();

        while (!session.isGameOver() && !session.hasArrived()) {

            GameDisplay.displayStatus(session);

            Double change = session.getKoChangeSincePrevious();
            if (change != null) {
                System.out.println();
                if (change > 0) {
                    System.out.printf("📈 Coca-Cola is up by $%.4f.%n", change);
                    System.out.println("Ration costs have increased.");
                } else if (change < 0) {
                    System.out.printf("📉 Coca-Cola is down by $%.4f.%n", Math.abs(change));
                    System.out.println("Ration costs have decreased.");
                } else {
                    System.out.println("📊 Coca-Cola hasn't moved.");
                    System.out.println("Ration costs remain stable.");
                }
            }

            GameDisplay.displayActionMenu();
            int action = GameDisplay.getPlayerChoice(1, 6);

            switch (action) {

                case 1 -> {
                    Landmark current = landmarks.get(session.getCurrentLandmark());
                    List<String> nextOptions = current.getNextLandmarkNames();

                    String chosenNext;
                    if (nextOptions.size() == 1) {
                        chosenNext = nextOptions.get(0);
                    } else {
                        GameDisplay.displayForkChoice(nextOptions.get(0), nextOptions.get(1));
                        int forkChoice = GameDisplay.getPlayerChoice(1, 2);
                        chosenNext = nextOptions.get(forkChoice - 1);
                    }

                    Landmark next = landmarks.get(chosenNext);
                    routeService.travel(session, next);
                    session.setCurrentLandmark(chosenNext);
                    session.addLandmarkVisited(chosenNext);
                    GameDisplay.displayArrival(next.getName(), next.getDescription(), next.getDistanceFromPrevious());

                    session.setFollowers(session.getFollowers() + next.getFollowerGain());
                    session.setInspiration(Math.min(
                            session.getInspiration() + next.getInspirationGain(),
                            GameConstants.MAX_INSPIRATION));

                    if (chosenNext.equals("Denver"))  new DenverEvent().execute(session);
                    if (chosenNext.equals("Reno"))    new RenoEvent().execute(session);
                    if (chosenNext.equals("Sidny"))   new SidnyEvent().execute(session);

                    if (chosenNext.equals("Pikes Peak")) {
                        new PikesPeakEvent().execute(session);
                        double koPrice2 = stockApi.getKoClosePrice();
                        session.setKoChangeSincePrevious(koPrice2 - session.getLastKoPrice());
                        session.setLastKoPrice(koPrice2);
                        session.setRationCostMultiplier(stockApi.getRationCostMultiplier(koPrice2));
                    }

                    if (chosenNext.equals("Santa Clara School of Law")) {
                        double koPrice3 = stockApi.getKoClosePrice();
                        session.setKoChangeSincePrevious(koPrice3 - session.getLastKoPrice());
                        session.setLastKoPrice(koPrice3);
                        session.setRationCostMultiplier(stockApi.getRationCostMultiplier(koPrice3));
                    }

                    if (chosenNext.equals("San Francisco")) {
                        session.setHasArrived(true);
                        GameDisplay.displayVictory(session);
                        new ScoringService().calculateFinalScore(session);
                    }
                }

                case 2 -> {
                    session.setRations(session.getRations() - 1);
                    session.setInspiration(Math.min(session.getInspiration() + 10, GameConstants.MAX_INSPIRATION));
                    session.setDaysElapsed(session.getDaysElapsed() + 1);
                    System.out.printf("%nThe team rests. Inspiration +10, Rations -1.%n");
                }

                case 3 -> {
                    session.setAiTokens(session.getAiTokens() + 5);
                    session.setCash(session.getCash() - 500);
                    session.setDaysElapsed(session.getDaysElapsed() + 1);
                    System.out.printf("%nThe team grinds. AI Tokens +5, Cash -$500.%n");
                }

                case 4 -> {
                    session.setConnections(session.getConnections() + 1);
                    session.setCash(session.getCash() - 300);
                    session.setDaysElapsed(session.getDaysElapsed() + 1);
                    System.out.printf("%nYou worked the room. Connections +1, Cash -$300.%n");
                }

                case 5 -> persistenceService.saveGame(session);

                case 6 -> {
                    System.out.printf("%nReturning to main menu...%n");
                    session.setGameOver(true);
                }
            }

            boolean eventFired = randomEventService.maybeFireEvent(session);
            if (eventFired) GameDisplay.waitForEnter();

            updateInspirationStreak(session);
            gameService.checkDangerZone(session);
            gameService.checkLosingConditions(session);
        }
    }

    private static void updateInspirationStreak(GameSession session) {
        int inspiration = session.getInspiration();
        if (inspiration < GameConstants.LOW_INSPIRATION_THRESHOLD) {
            session.setLowInspirationStreak(session.getLowInspirationStreak() + 1);
            session.setHighInspirationStreak(0);
        } else if (inspiration > GameConstants.HIGH_INSPIRATION_THRESHOLD) {
            session.setHighInspirationStreak(session.getHighInspirationStreak() + 1);
            session.setLowInspirationStreak(0);
        } else {
            session.setLowInspirationStreak(0);
            session.setHighInspirationStreak(0);
        }
    }
}