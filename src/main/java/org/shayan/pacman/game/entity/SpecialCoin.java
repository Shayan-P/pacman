package org.shayan.pacman.game.entity;

import javafx.scene.image.Image;
import org.shayan.pacman.game.GameWorld;

public class SpecialCoin extends Coin {
    private static final Image[] coinImages;

    static {
        coinImages = new Image[7];
        for(int i = 0; i < 7; i++)
            coinImages[i] = getImageInResource(String.format("/coin/special/%d.png", i));
    }

    @Override
    public double getR() {
        return 0.6 * world.getBlockLength() / 2;
    }

    public SpecialCoin(GameWorld world, double x, double y) {
        super(world, coinImages, x, y);
    }
}
