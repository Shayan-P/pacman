package org.shayan.pacman.game.entity.ai;

import javafx.scene.image.Image;
import org.shayan.pacman.game.GameWorld;
import org.shayan.pacman.game.entity.MovingEntity;
import org.shayan.pacman.game.event.GhostEatEvent;
import org.shayan.pacman.menu.GameMenu;

abstract public class AI extends MovingEntity {
    @Override
    public double getR() {
        return 0.9 * GameWorld.getInstance().getBlockLength() / 2;
    }

    private void collisionWithPacman(){
        if (this.getManhattanDistance(GameWorld.getInstance().getPacman()) <= 4) {
            GameMenu.getInstance().fireEvent(new GhostEatEvent(this));
        }
    }

    @Override
    protected void conditionsInMovement() {
        collisionWithPacman();
    }

    public AI(int skinId, double x, double y){
        super(
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
