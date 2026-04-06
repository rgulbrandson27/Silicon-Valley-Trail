package com.raina.siliconvalleytrail.model;

import com.raina.siliconvalleytrail.service.event.anchored.AnchoredEvent;

import java.util.List;

public class Landmark {

    private final String name;
    private final String description;
    private final LandmarkType type;
    private final Region region;
    private final int distanceFromPrevious;
    private final boolean isForkPoint;
    private final int followerGain;
    private final int inspirationGain;
    private final double latitude;
    private final double longitude;
    private final int milesToSanFrancisco;
    private final AnchoredEvent anchoredEvent;
    private final List<String> nextLandmarkNames;


    public Landmark(String name, String description, LandmarkType type,
                    Region region, int distanceFromPrevious, boolean isForkPoint,
                    int followerGain, int inspirationGain,
                    double latitude, double longitude,
                    int milesToSanFrancisco, AnchoredEvent anchoredEvent,
                    List<String> nextLandmarkNames) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.region = region;
        this.distanceFromPrevious = distanceFromPrevious;
        this.isForkPoint = isForkPoint;
        this.followerGain = followerGain;
        this.inspirationGain = inspirationGain;
        this.latitude = latitude;
        this.longitude = longitude;
        this.milesToSanFrancisco = milesToSanFrancisco;
        this.nextLandmarkNames = nextLandmarkNames;
        this.anchoredEvent = anchoredEvent;
        }

    // getters — no setters, landmarks are immutable
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LandmarkType getType() { return type; }
    public Region getRegion() { return region; }
    public int getDistanceFromPrevious() { return distanceFromPrevious; }
    public boolean isForkPoint() { return isForkPoint; }
    public int getFollowerGain() { return followerGain; }
    public int getInspirationGain() { return inspirationGain; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public int getMilesToSanFrancisco() { return milesToSanFrancisco; }
    public AnchoredEvent anchoredEvent() {return anchoredEvent; }
    public List<String> getNextLandmarkNames() { return nextLandmarkNames; }
}

