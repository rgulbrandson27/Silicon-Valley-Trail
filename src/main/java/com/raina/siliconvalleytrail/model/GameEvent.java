package com.raina.siliconvalleytrail.model;

public abstract class GameEvent {

    private String description;
    private int cashImpact;
    private int connectionsImpact;
    private int inspirationImpact;
    private int daysImpact;
    private int energySourceImpact;
    private boolean isSteepLearningCurve;

    public GameEvent(String description, int cashImpact, int connectionsImpact,
                     int inspirationImpact, int daysImpact, int energyImpact) {
        this.description = description;
        this.cashImpact = cashImpact;
        this.connectionsImpact = connectionsImpact;
        this.inspirationImpact = inspirationImpact;
        this.daysImpact = daysImpact;
        this.energyImpact = energyImpact;
    }

    // abstract method - every subclass MUST implement this
    public abstract void apply(GameSession session);

    // getters
    public String getDescription() { return description; }
    public int getCashImpact() { return cashImpact; }
    public int getConnectionsImpact() { return connectionsImpact; }
    public int getInspirationImpact() { return inspirationImpact; }
    public int getDaysImpact() { return daysImpact; }
    public int getEnergyImpact() { return energyImpact; }
}