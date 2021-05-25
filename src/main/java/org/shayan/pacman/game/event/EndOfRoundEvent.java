package org.shayan.pacman.game.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class EndOfRoundEvent extends Event {
    public static final EventType<EndOfRoundEvent> MY_TYPE = new EventType<>("end of round event");

    public EndOfRoundEvent() {
        super(MY_TYPE);
    }
}
