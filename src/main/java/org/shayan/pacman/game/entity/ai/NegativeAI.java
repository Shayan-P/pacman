package org.shayan.pacman.game.entity.ai;

import javafx.animation.KeyFrame;
import javafx.util.Duration;
import org.shayan.pacman.game.GameWorld;

public class NegativeAI extends AI {
    @Override
    void setStrategy() {
        setLoop(new KeyFrame(Duration.millis(50), e->{
            setDirection(-world.getPacman().getVX(), -world.getPacman().getVY());
        }));
    }

    public NegativeAI(GameWorld world, int skinId, double x, double y){
        super(world, skinId, x, y);
        setStrategy();
    }
}
