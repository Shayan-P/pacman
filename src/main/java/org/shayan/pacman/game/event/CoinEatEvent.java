package org.shayan.pacman.game.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import org.shayan.pacman.game.entity.Coin;

public class CoinEatEvent extends Event {
    public static final EventType<CoinEatEvent> MY_TYPE = new EventType<>("coin eat event");

    private Coin coin;

    public Coin getCoin() {
        return coin;
    }

    public CoinEatEvent(Coin coin) {
        super(MY_TYPE);
        this.coin = coin;
    }
}
