package org.shayan.pacman.game.entity.ai;

import javafx.animation.KeyFrame;
import javafx.util.Duration;
import org.shayan.pacman.game.GameWorld;

public class LineChaserAI extends AI {
    @Override
    void setStrategy() {
        setLoop(new KeyFrame(Duration.millis(50), e->{
            double dirX = GameWorld.getInstance().getPacman().getX() - getX();
            double dirY = GameWorld.getInstance().getPacman().getY() - getY();
            double len = Math.sqrt(dirX * dirX + dirY * dirY);
            dirX/= len;
            dirY/= len;
            // todo do not crash when it becomes 0
            dirX *= 2;
            dirY *= 2;

            setFX(dirX);
            setFY(dirY);
        }));
    }

    public LineChaserAI(double x, double y){
        super(x, y);
    }
}
