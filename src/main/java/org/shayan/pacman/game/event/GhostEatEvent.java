package org.shayan.pacman.game.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import org.shayan.pacman.game.entity.ai.AI;

public class GhostEatEvent extends Event {
    public static final EventType<GhostEatEvent> MY_TYPE = new EventType<>("ghost eat event");

    private AI ai;

    public AI getAI() {
        return ai;
    }

    public GhostEatEvent(AI ai) {
        super(MY_TYPE);
        this.ai = ai;
    }
}
