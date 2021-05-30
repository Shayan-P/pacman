package org.shayan.pacman.game.entity;

import org.shayan.pacman.game.GameWorld;

public class Wall extends Entity{
    @Override
    public double getR() {
        return world.getBlockLength() * 0.48;
    }

    public Wall(GameWorld world, double x, double y) {
        super(world, getImageInResource("/wall.png"), x, y);
    }
}
