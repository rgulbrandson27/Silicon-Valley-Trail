package com.raina.siliconvalleytrail.util;
import com.raina.siliconvalleytrail.model.GameSession;

import java.util.Scanner;

public class GameDisplay {

    public static void displayWelcomeMessage() {
        System.out.printf("╔══════════════════════════════════════╗%n");
        System.out.printf("║        SILICON VALLEY TRAIL  🚀      ║%n");
        System.out.printf("║  Can your startup survive the ride?  ║%n");
        System.out.printf("╚══════════════════════════════════════╝%n");
        waitForEnter();
    }

    public static void displayGameSummary() {
        System.out.printf("YOUR MISSION:%n");
        System.out.printf("─────────────────────────────────────%n");
        System.out.printf("You are a scrappy startup founder%n");
        System.out.printf("heading west to compete in the%n");
        System.out.printf("Startup World Cup Grand Finale%n");
        System.out.printf("in San Francisco on November 18, 2026.%n");
        waitForEnter();
    }

    public static void displayActionMenu() {
        System.out.printf("%nWhat will you do?%n");
        System.out.printf("─────────────────────────────────────%n");
        System.out.printf("1. Travel to next landmark%n");
        System.out.printf("2. Rest and recover (restore inspiration, use rations)%n");
        System.out.printf("3. Work on product (gain AI tokens, costs cash)%n");
        System.out.printf("4. Network (gain connections, costs cash)%n");
        System.out.printf("5. Save game%n");
        System.out.printf("6. Quit to menu%n");
        System.out.printf("─────────────────────────────────────%n");
        System.out.print("Enter choice (1-6): ");
    }

    public static void displayArrival(String landmarkName, String description, int milesTraveled) {
        System.out.printf("%n✅ Arrived at %s!%n", landmarkName);
        System.out.printf("🚗 Miles traveled: %d%n", milesTraveled);
        System.out.printf("%s%n", description);
    }
    public static void displayForkChoice(String option1, String option2) {
        System.out.printf("%nYou face a fork in the road!%n");
        System.out.printf("─────────────────────────────────────%n");
        System.out.printf("1. %s%n", option1);
        System.out.printf("2. %s%n", option2);
        System.out.printf("─────────────────────────────────────%n");
        System.out.printf("Enter choice (1-2): ");
    }

    public static void displayGameOver(String message, GameSession session) {
        System.out.printf("%n╔══════════════════════════════════════╗%n");
        System.out.printf("║            GAME OVER 💀               ║%n");
        System.out.printf("╚══════════════════════════════════════╝%n");
        System.out.printf("%s%n", message);
        System.out.printf("Days elapsed: %d%n", session.getDaysElapsed());
        System.out.printf("Cash remaining: $%,d%n", session.getCash());
        System.out.printf("Landmarks visited: %d%n", session.getLandmarksVisited().size());
    }

    public static void displayVictory(GameSession session) {
        System.out.printf("%n╔══════════════════════════════════════╗%n");
        System.out.printf("║         YOU MADE IT! 🏆               ║%n");
        System.out.printf("╚══════════════════════════════════════╝%n");
        System.out.printf("Welcome to San Francisco, %s!%n", session.getFounderName());
        System.out.printf("Days taken: %d%n", session.getDaysElapsed());
        System.out.printf("Cash remaining: $%,d%n", session.getCash());
        System.out.printf("Followers: %,d%n", session.getFollowers());
    }

    public static int getPlayerChoice(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice < min || choice > max) {
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice < min || choice > max) {
                    System.out.printf("Please enter a number between %d and %d: ", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.printf("Invalid input. Enter a number between %d and %d: ", min, max);
            }
        }
        return choice;
    }

    public static void displayInstructionsPage2() {
        System.out.printf("YOUR RESOURCES:%n");
        System.out.printf("─────────────────────────────────────%n");
        System.out.printf("💰 Cash       - your runway%n");
        System.out.printf("🤝 Connections - open doors%n");
        System.out.printf("📱 Followers  - social clout%n");
        System.out.printf("✨ Inspiration - team morale%n");
        waitForEnter();
    }

    public static void displayInstructionsPage3() {
        System.out.printf("WATCH OUT:%n");
        System.out.printf("─────────────────────────────────────%n");
        System.out.printf("⚠️  Run out of cash = game over%n");
        System.out.printf("⚠️  Inspiration below 20 for 3 days = burnout%n");
        System.out.printf("⚠️  Inspiration above 80 for 3 days = burnout%n");
        System.out.printf("⚠️  Miss the deadline = game over%n");
        waitForEnter();
    }

    public static void displayStatus(GameSession session) {
        System.out.printf("%n─────────────────────────────────────%n");
        System.out.printf("Day %d | %s | Days remaining: %d%n",
                session.getDaysElapsed(),
                session.getCurrentLandmark(),
                session.getDaysRemaining());
        System.out.printf("─────────────────────────────────────%n");
        System.out.printf("💰 Cash:        $%,d%n", session.getCash());
        System.out.printf("🎒 Rations:     %d days%n", session.getRations());
        System.out.printf("🔗 Connections: %d%n", session.getConnections());
        System.out.printf("📱 Followers:   %,d%n", session.getFollowers());
        System.out.printf("🤖 AI Tokens:   %d%n", session.getAiTokens());
        System.out.printf("✨ Inspiration: %d%n", session.getInspiration());
        System.out.printf("🗺️  Miles left:  %d%n", session.getMilesRemaining());
        System.out.printf("─────────────────────────────────────%n");
    }

    public static String getValidatedName(String prompt, int maxLength) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (input.trim().isEmpty()) {
            System.out.printf("%s: ", prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("This field cannot be empty. Please try again.");
            } else if (input.length() > maxLength) {
                System.out.printf("Too long! Maximum %d characters. Please try again.%n", maxLength);
                input = "";
            }
        }
        return input;
    }

    public static String getPlayerName(String prompt) {
        System.out.printf("%s: ", prompt);
        return new Scanner(System.in).nextLine().trim();
    }

    public static void waitForEnter() {
        System.out.printf("%nPress ENTER to continue...%n");
        new Scanner(System.in).nextLine();
    }
}
