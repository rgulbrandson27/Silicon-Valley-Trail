package com.raina.siliconvalleytrail.model;

import com.raina.siliconvalleytrail.util.GameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameSession {

    private String sessionId;

    private String founderName;
    private String engineer1Name;
    private String engineer2Name;
    private String engineer3Name;

    // resources
    private int cash;
    private int rations;
    private int connections;
    private int followers;
    private int aiTokens;

    // team status
    private int inspiration;
    private LearningCurve learningCurve;

    // time
    private DepartureDate departureDate;
    private int totalDays;
    private int daysElapsed;

    // travel progress
    private String currentLandmark;
    private List<String> landmarksVisited;
    private int milesRemaining;
    private Region currentRegion;

    // scoring
    private int dangerZoneCount;      // cash danger zone hits
    private int startingCash;         // to compare at end
    private boolean gameOver;
    private boolean hasArrived;
    private CompetitionResult competitionResult;  // null until final destination arrival

    // inspiration streak trackers
    private int lowInspirationStreak;   // days below 20
    private int highInspirationStreak;  // days above 80

    // constructor
    public GameSession(String founderName, String engineer1Name, String engineer2Name,
                       String engineer3Name, DepartureDate departureDate,
                       int totalDays, int startingCash) {
        this.sessionId = UUID.randomUUID().toString();
        this.founderName = founderName;
        this.engineer1Name = engineer1Name;
        this.engineer2Name = engineer2Name;
        this.engineer3Name = engineer3Name;
        this.departureDate = departureDate;
        this.totalDays = totalDays;
        this.startingCash = startingCash;
        this.cash = startingCash;
        this.rations = totalDays / 2;
        this.connections = 0;
        this.followers = 450;
        this.aiTokens = 0;
        this.inspiration = 50;
        this.learningCurve = LearningCurve.STEADY;
        this.daysElapsed = 0;
        this.milesRemaining = GameConstants.DEFAULT_ROUTE_MILES;
        this.currentLandmark = null;
        this.landmarksVisited = new ArrayList<>();
        this.currentRegion = Region.GREAT_PLAINS;  // always start in Nebraska
        this.dangerZoneCount = 0;
        this.gameOver = false;
        this.hasArrived = false;
        this.competitionResult = null;
        this.lowInspirationStreak = 0;
        this.highInspirationStreak = 0;
    }

    // derived and does not go in "Service" because it only uses variables from this GameSession class
    public int getDaysRemaining() {
        return totalDays - daysElapsed;
    }

    // identity
    public String getSessionId() { return sessionId; }

    public String getFounderName() { return founderName; }
    public void setFounderName(String founderName) { this.founderName = founderName; }

    public String getEngineer1Name() { return engineer1Name; }
    public void setEngineer1Name(String engineer1Name) { this.engineer1Name = engineer1Name; }

    public String getEngineer2Name() { return engineer2Name; }
    public void setEngineer2Name(String engineer2Name) { this.engineer2Name = engineer2Name; }

    public String getEngineer3Name() { return engineer3Name; }
    public void setEngineer3Name(String engineer3Name) { this.engineer3Name = engineer3Name; }

    // resources
    public int getCash() { return cash; }
    public void setCash(int cash) { this.cash = cash; }

    public int getRations() { return rations; }
    public void setRations(int rations) { this.rations = rations; }

    public int getConnections() { return connections; }
    public void setConnections(int connections) { this.connections = connections; }

    public int getFollowers() { return followers; }
    public void setFollowers(int followers) { this.followers = followers; }

    public int getAiTokens() { return aiTokens; }
    public void setAiTokens(int aiTokens) { this.aiTokens = aiTokens; }

    // team status
    public int getInspiration() { return inspiration; }
    public void setInspiration(int inspiration) { this.inspiration = inspiration; }

    public LearningCurve getLearningCurve() { return learningCurve; }
    public void setLearningCurve(LearningCurve learningCurve) { this.learningCurve = learningCurve; }

    // time
    public DepartureDate getDepartureDate() { return departureDate; }
    public void setDepartureDate(DepartureDate departureDate) { this.departureDate = departureDate; }

    public int getTotalDays() { return totalDays; }

    public int getDaysElapsed() { return daysElapsed; }
    public void setDaysElapsed(int daysElapsed) { this.daysElapsed = daysElapsed; }

    // travel progress
    public String getCurrentLandmark() { return currentLandmark; }
    public void setCurrentLandmark(String currentLandmark) { this.currentLandmark = currentLandmark; }

    public List<String> getLandmarksVisited() { return landmarksVisited; }
    public void addLandmarkVisited(String landmark) { this.landmarksVisited.add(landmark); }

    public int getMilesRemaining() { return milesRemaining; }
    public void setMilesRemaining(int milesRemaining) { this.milesRemaining = milesRemaining; }

    public Region getCurrentRegion() { return currentRegion; }
    public void setCurrentRegion(Region currentRegion) { this.currentRegion = currentRegion; }

    // scoring
    public int getDangerZoneCount() { return dangerZoneCount; }
    public void incrementDangerZoneCount() { this.dangerZoneCount++; }

    public int getStartingCash() { return startingCash; }

    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    public boolean hasArrived() { return hasArrived; }
    public void setHasArrived(boolean hasArrived) { this.hasArrived = hasArrived; }

    public CompetitionResult getCompetitionResult() { return competitionResult; }
    public void setCompetitionResult(CompetitionResult competitionResult) {
        this.competitionResult = competitionResult;
    }

    // inspiration streaks
    public int getLowInspirationStreak() { return lowInspirationStreak; }
    public void setLowInspirationStreak(int lowInspirationStreak) {
        this.lowInspirationStreak = lowInspirationStreak;
    }

    public int getHighInspirationStreak() { return highInspirationStreak; }
    public void setHighInspirationStreak(int highInspirationStreak) {
        this.highInspirationStreak = highInspirationStreak;
    }
}