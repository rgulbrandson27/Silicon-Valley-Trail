package com.raina.siliconvalleytrail.model;

public abstract class GameEvent {

    private final String description;
    private final int cashImpact;
    private final int connectionsImpact;
    private final int followersImpact;
    private final int rationImpact;
    private final int inspirationImpact;
    private final int aiTokensImpact;
    private final int daysImpact;

    public GameEvent(String description, int cashImpact, int connectionsImpact,
                     int followersImpact, int rationImpact, int inspirationImpact,
                     int aiTokensImpact, int daysImpact) {
        this.description = description;
        this.cashImpact = cashImpact;
        this.connectionsImpact = connectionsImpact;
        this.followersImpact = followersImpact;
        this.rationImpact = rationImpact;
        this.inspirationImpact = inspirationImpact;
        this.aiTokensImpact = aiTokensImpact;
        this.daysImpact = daysImpact;
    }

    // abstract method — every subclass MUST implement this
    public abstract void apply(GameSession session);

    // getters — no setters, events are immutable like landmarks
    public String getDescription() { return description; }
    public int getCashImpact() { return cashImpact; }
    public int getConnectionsImpact() { return connectionsImpact; }
    public int getFollowersImpact() { return followersImpact; }
    public int getRationImpact() { return rationImpact; }
    public int getInspirationImpact() { return inspirationImpact; }
    public int getAiTokensImpact() { return aiTokensImpact; }
    public int getDaysImpact() { return daysImpact; }
}