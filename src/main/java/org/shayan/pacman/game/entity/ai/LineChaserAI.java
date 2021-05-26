package org.shayan.pacman.game.entity.ai;

import javafx.animation.KeyFrame;
import javafx.util.Duration;
import org.shayan.pacman.game.GameWorld;

public class LineChaserAI extends AI {
    @Override
    void setStrategy() {
        setLoop(new KeyFrame(Duration.millis(50), e->{
            setDirection(GameWorld.getInstance().getPacman().getX() - getX(), GameWorld.getInstance().getPacman().getY() - getY());
        }));
    }

    public LineChaserAI(int skinId, double x, double y){
        super(skinId, x, y);
        setStrategy();
    }
}
