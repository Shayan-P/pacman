package org.shayan.pacman.game.entity;

import javafx.scene.image.Image;
import org.shayan.pacman.game.GameWorld;

public class Coin extends MovingEntity {
    private static final Image[] coinImages;

    @Override
    public double getR() {
        return 0.9 * world.getBlockLength() / 2;
    }

    static {
        coinImages = new Image[8];
        for(int i = 0; i < 8; i++)
            coinImages[i] = getImageInResource(String.format("/coin/%d.png", i));
    }

    public Coin(GameWorld world, Image[] images , double x, double y){
        super(world, images, null, x, y);
    }

    public Coin(GameWorld world, double x, double y){
        this(world, coinImages, x, y);
    }

    @Override
    protected void conditionsInMovement() {
    }
}
