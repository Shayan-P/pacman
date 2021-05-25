package org.shayan.pacman.game.entity;

import org.shayan.pacman.game.GameWorld;

public class Wall extends Entity{
    @Override
    public double getR() {
        return GameWorld.getInstance().getBlockLength() / 2;
    }

    public Wall(double x, double y) {
        super(getImageInResource("/wall.png"), x, y);
    }
}
