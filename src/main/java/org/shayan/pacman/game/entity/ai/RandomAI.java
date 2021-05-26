package org.shayan.pacman.game.entity.ai;

import javafx.animation.KeyFrame;
import javafx.util.Duration;
import org.shayan.pacman.game.GameWorld;

import java.util.Random;

public class RandomAI extends AI {
    private final Random rnd = new Random();

    @Override
    void setStrategy() {
        setLoop(new KeyFrame(Duration.millis(100), e->{
            while(world.getWalls().stream().anyMatch(this::willHaveCollision))
                setDirection(rnd.nextDouble() % 1, rnd.nextDouble() % 1);
        }));
    }

    public RandomAI(GameWorld world, int skinId, double x, double y){
        super(world, skinId, x, y);
        setStrategy();
    }
}
