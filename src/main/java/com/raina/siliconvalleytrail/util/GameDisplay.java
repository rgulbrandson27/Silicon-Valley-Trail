package com.raina.siliconvalleytrail.util;

import com.raina.siliconvalleytrail.model.GameSession;
import java.util.Scanner;

public class
GameDisplay {

    private static final Scanner scanner = new Scanner(System.in);

    public static void displayWelcomeMessage() {
        System.out.printf("╔══════════════════════════════════════╗%n");
        System.out.printf("║        SILICON VALLEY TRAIL  🚗      ║%n");
        System.out.printf("║       San Francisco or Bust!         ║%n");
        System.out.printf("╚══════════════════════════════════════╝%n");
    }

    public static void displayGameSummary() {
        System.out.printf("           YOUR MISSION:%n");
        System.out.printf("──────────────────────────────────────────%n");
        System.out.printf("You are the founder of a scrappy startup team%n");
        System.out.printf("with a discreet make-shift office%n");
        System.out.printf("in the SILICON PRAIRIE!%n%n");
        System.out.printf("It may only be an abandoned dorm room on the university's%n");
        System.out.printf("campus, but it works, and the internet speed is great!%n");
        System.out.printf("Most importantly - it is the origin of a tech product with%n");
        System.out.printf("the potential to change history.%n");

        System.out.printf("Just one problem...you need startup $$$. %n");
        System.out.printf("The only thing getting in the way of%n");
        System.out.printf("this product launch is halfway across the country.%n");

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       STARTUP WORLD CUP FINALE           ║");
        System.out.println("║           San Francisco, CA              ║");
        System.out.println("║          November 18–20, 2026            ║");
        System.out.println("║                                          ║");
        System.out.println("║           💰 GRAND PRIZE: 💰             ║");
        System.out.println("║          ONE MILLION DOLLARS!            ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    public static void displayInstructionsPage2() {
        System.out.printf("Other than ingenuity, there is not much%n");
        System.out.printf("to work with, but you do have the following:%n");
        System.out.printf("─────────────────────────────────────%n");
        System.out.printf("RESOURCES──────────────%n");
        System.out.printf("💰 Cash - $10,000.   %n");
        System.out.printf("🤝 Connections - 2%n");
        System.out.printf("📱 Social Media Followers - 450 (campus prank video)%n");
        System.out.printf("🤖 AI Tokens - 250,000%n");
        System.out.printf("STATUS───────────────────────%n");
        System.out.printf("✨ Inspiration - 50/100%n");
        System.out.printf("📈 Learning Curve - STEADY%n");
        System.out.printf("─────────────────────────────────────%n");

    }

    public static void displayInstructionsPage3() {
        System.out.printf("TO STAY IN THE COMPETITION....%n");
        System.out.printf("─────────────────────────────────────%n");
        System.out.printf("⚠️  Don't run out of cash!%n");
        System.out.printf("⚠️  Don't run out of rations!%n");
        System.out.printf("⚠️  Monitor your excitement and productivity levels%n");
        System.out.printf("⚠️  Inspiration above 90%% for 2 days without pivot = team burnout%n");
        System.out.printf("⚠️  Inspiration below 20%% for 2 days without an uplift = no steam%n");
        System.out.printf("⚠️  And of course, DON'T BE LATE for the competition!%n");
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
        System.out.printf("🚗 Miles traveled this leg: %d%n", milesTraveled);
        System.out.printf("%s%n", description);
    }

    public static void displayForkChoice(String option1, String option2) {
        System.out.printf("%nTime to make a decision!%n");
        System.out.printf("%nKeep heading to Denver and stop by Cabela's in Sidny, NE%n");
        System.out.printf("   OR%n");
        System.out.printf("Deepen your Nebraskan roots with a detour to Chimney Rock%n");
        System.out.printf("─────────────────────────────────────%n");
        System.out.printf("1. %s%n", option1);
        System.out.printf("2. %s%n", option2);
        System.out.printf("─────────────────────────────────────%n");
        System.out.printf("Enter choice (1-2): ");
    }

    public static void displayGameOver(String message, GameSession session) {
        System.out.printf("%n╔══════════════════════════════════════╗%n");
        System.out.printf("║            GAME OVER 💀              ║%n");
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
        System.out.printf("🤖 AI Tokens:   %,d%n", session.getAiTokens());
        System.out.printf("✨ Inspiration: %d/100%n", session.getInspiration());
        System.out.printf("🗺️  Miles left:  %d%n", session.getMilesRemaining());
        System.out.printf("─────────────────────────────────────%n");
    }

    public static int getPlayerChoice(int min, int max) {
        int choice = -1;
        while (choice < min || choice > max) {
            try {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;  // ← add this
                choice = Integer.parseInt(line);
                if (choice < min || choice > max) {
                    System.out.printf("Please enter a number between %d and %d: ", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.printf("Invalid input. Enter a number between %d and %d: ", min, max);
            }
        }
        return choice;
    }

    public static String getValidatedName(String prompt, int maxLength) {
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
        return scanner.nextLine().trim();
    }

    public static void waitForEnter() {
        System.out.printf("%nPress ENTER to continue...%n");
        scanner.nextLine();
    }
}