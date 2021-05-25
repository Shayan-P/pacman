package org.shayan.pacman.game.entity;

import javafx.scene.image.Image;
import org.shayan.pacman.game.GameWorld;

public class Coin extends MovingEntity {
    @Override
    public double getR() {
        return 0.9 * GameWorld.getInstance().getBlockLength() / 2;
    }

    private static Image[] getCoinImages(){
        Image[] coins = new Image[8];
        for(int i = 0; i < 8; i++)
            coins[i] = getImageInResource(String.format("/coin/%d.png", i));
        return coins;
    }

    public Coin(double x, double y){
        super(getCoinImages(), null, x, y);
    }

    @Override
    protected void conditionsInMovement() {
    }
}
