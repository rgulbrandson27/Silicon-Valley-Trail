package com.raina.siliconvalleytrail.model;

import com.raina.siliconvalleytrail.model.GameSession;

public abstract class LandmarkEvent {

    // every landmark event must implement this
    public abstract void execute(GameSession session);

    // shared utility for randomness
    protected boolean randomChance(int outOf) {
        return new java.util.Random().nextInt(outOf) == 0;
    }
}