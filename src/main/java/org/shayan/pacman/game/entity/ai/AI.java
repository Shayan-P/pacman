package org.shayan.pacman.game.entity.ai;

import javafx.scene.image.Image;
import org.shayan.pacman.game.GameWorld;
import org.shayan.pacman.game.entity.MovingEntity;
import org.shayan.pacman.game.event.GhostEatEvent;
import org.shayan.pacman.menu.GameMenu;

abstract public class AI extends MovingEntity {
    @Override
    public double getR() {
        return 0.9 * world.getBlockLength() / 2;
    }

    private void collisionWithPacman(){
        if (this.getManhattanDistance(world.getPacman()) <= 4) {
            world.fireEvent(new GhostEatEvent(this));
        }
    }

    @Override
    protected void conditionsInMovement() {
        collisionWithPacman();
    }

    public AI(GameWorld world, int skinId, double x, double y){
        super(
                world,
                new Image[]{getImageInResource(String.format("/ais/%d/front.png", skinId))},
                new Image[]{getImageInResource(String.format("/ais/%d/right.png", skinId))},
                x, y
        );
    }

    @Override
    public double getSpeedCof() {
        return 1.7;
    }

    protected void setDirection(double dirX, double dirY){
        double len = Math.sqrt(dirX * dirX + dirY * dirY);
        if(len == 0)
            return;
        setFX(dirX / len);
        setFY(dirY / len);
    }

    abstract void setStrategy();
}
