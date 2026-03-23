package com.raina.siliconvalleytrail.util;

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


    public static void waitForEnter() {
        System.out.printf("%nPress ENTER to continue...%n");
        new Scanner(System.in).nextLine();
    }
}
