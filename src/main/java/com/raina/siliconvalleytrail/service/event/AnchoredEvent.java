package com.raina.siliconvalleytrail.service.event;

import com.raina.siliconvalleytrail.model.GameSession;

public abstract class AnchoredEvent {

    // AnchoredEvent — abstract superclass for events tied to a specific landmark
    // Example subclasses: DenverEvent, RenoEvent
    public abstract void execute(GameSession session);

    // shared utility for randomness
    protected boolean randomChance(int outOf) {
        return new java.util.Random().nextInt(outOf) == 0;
    }
}