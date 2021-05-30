package org.shayan.pacman.game.entity.ai;

import javafx.animation.KeyFrame;
import javafx.util.Duration;
import org.shayan.pacman.game.GameWorld;

import java.util.Random;

public class RandomAI extends AI {
    private final Random rnd = new Random();

    @Override
    void setStrategy() {
        setLoop(new KeyFrame(Duration.millis(800), e->{
            int tries = 10;
            while(((getVX() == 0 && getVY() == 0) || willHaveCollisionWithWalls()) && tries > 0) {
                setDirection(rnd.nextDouble() % 1, rnd.nextDouble() % 1);
                tries--;
            }
            if(willHaveCollisionWithWalls())
                setDirection(1, 0);
            else
                return;
            if(willHaveCollisionWithWalls())
                setDirection(-1, 0);
            else
                return;
            if(willHaveCollisionWithWalls())
                setDirection(0, 1);
            else
                return;
            if(willHaveCollisionWithWalls())
                setDirection(0, -1);
            else
                return;
        }));
    }

    public RandomAI(GameWorld world, int skinId, double x, double y){
        super(world, skinId, x, y);
        setStrategy();
    }
}
