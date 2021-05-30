package org.shayan.pacman.game.entity.ai;

import javafx.scene.image.Image;
import org.shayan.pacman.game.GameWorld;
import org.shayan.pacman.game.entity.MovingEntity;
import org.shayan.pacman.game.event.GhostEatEvent;

abstract public class AI extends MovingEntity {
    private int skinId;

    @Override
    public double getR() {
        return 0.9 * world.getBlockLength() / 2;
    }

    private void collisionWithPacman(){
        if (this.getManhattanDistance(world.getPacman()) <= 0.6 * world.getBlockLength()) {
            world.fireEvent(new GhostEatEvent(this));
        }
    }

    @Override
    protected void conditionsInMovement() {
        collisionWithPacman();
    }

    private void changeSkin(int skinId){
        rightImages = new Image[]{getImageInResource(String.format("/ais/%d/right.png", skinId))};
        frontImages = new Image[]{getImageInResource(String.format("/ais/%d/front.png", skinId))};
    }

    public void beScared(){
        changeSkin(4);
    }
    public void beNormal(){
        changeSkin(this.skinId);
    }

    public AI(GameWorld world, int skinId, double x, double y){
        super(
                world,
                new Image[]{getImageInResource(String.format("/ais/%d/right.png", skinId))},
                new Image[]{getImageInResource(String.format("/ais/%d/front.png", skinId))},
                x, y
        );
        this.skinId = skinId;
    }

    @Override
    public double getSpeedCof() {
        return 1.7;
    }

    protected void setDirection(double dirX, double dirY){
        double len = Math.sqrt(dirX * dirX + dirY * dirY);
        if(len == 0)
            return;
        if(world.getPacman().isSuperManMode()) {
            setFX(-dirX / len);
            setFY(-dirY / len);
        } else{
            setFX(dirX / len);
            setFY(dirY / len);
        }
    }

    abstract void setStrategy();
}
