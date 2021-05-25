package org.shayan.pacman.game.event;

import javafx.event.Event;
import javafx.event.EventType;

public class PacmanWinsEvent extends Event {
    public static final EventType<PacmanWinsEvent> MY_TYPE = new EventType<>("pacman wins event");

    public PacmanWinsEvent() {
        super(MY_TYPE);
    }
}
