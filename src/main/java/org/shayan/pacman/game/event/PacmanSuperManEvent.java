package org.shayan.pacman.game.event;

import javafx.event.Event;
import javafx.event.EventType;

public class PacmanSuperManEvent extends Event {
    public static final EventType<PacmanSuperManEvent> MY_TYPE = new EventType<>("pacman superman event");

    private final boolean state;

    public boolean isSuperMan(){
        return state;
    }

    public PacmanSuperManEvent(boolean state) {
        super(MY_TYPE);
        this.state = state;
    }
}
