package com.raina.siliconvalleytrail.service;

import com.raina.siliconvalleytrail.model.GameSession;
import com.raina.siliconvalleytrail.model.Landmark;

public class TravelService {

    // called in game loop after each movement
    public void travel(GameSession session, Landmark next) {
        int travelCost = (int)(next.getDistanceFromPrevious() *
                session.getCurrentRegion().getCostMultiplier());
        session.setCash(session.getCash() - travelCost);
        session.setMilesRemaining(next.getMilesToSanFrancisco());
        session.setCurrentRegion(next.getRegion());
        session.setCurrentLandmark(next.getName());
        session.addLandmarkVisited(next.getName());
        session.setDaysElapsed(session.getDaysElapsed() + 1);
        session.setRations(session.getRations() - 1);
    }
}