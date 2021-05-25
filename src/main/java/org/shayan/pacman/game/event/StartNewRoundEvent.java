package org.shayan.pacman.game.event;

import javafx.event.Event;
import javafx.event.EventType;

public class StartNewRoundEvent extends Event {
    public static final EventType<StartNewRoundEvent> MY_TYPE = new EventType<>("start new round event");

    public StartNewRoundEvent() {
        super(MY_TYPE);
    }

}
