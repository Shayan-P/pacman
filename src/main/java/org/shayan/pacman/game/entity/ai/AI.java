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

    public AI(double x, double y){
        super(new Image[]{getImageInResource("/ai1/0.png")}, new Image[]{getImageInResource("/ai1/0.png")}, x, y);
        setStrategy();
    }

    abstract void setStrategy();
}
