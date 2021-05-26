package org.shayan.pacman.game.entity.ai;

import javafx.animation.KeyFrame;
import javafx.util.Duration;
import org.shayan.pacman.game.GameWorld;

public class LineChaserAI extends AI {
    @Override
    void setStrategy() {
        setLoop(new KeyFrame(Duration.millis(50), e->{
            setDirection(world.getPacman().getX() - getX(), world.getPacman().getY() - getY());
        }));
    }

    public LineChaserAI(GameWorld world, int skinId, double x, double y){
        super(world, skinId, x, y);
        setStrategy();
    }
}
