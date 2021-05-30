package org.shayan.pacman.game.entity;

import javafx.animation.KeyFrame;
import javafx.scene.image.Image;
import javafx.util.Duration;
import org.shayan.pacman.game.GameWorld;

import java.util.concurrent.atomic.AtomicInteger;

abstract public class MovingEntity extends Entity {
    private double fX = 0, fY = 0;

    protected Image[] frontImages, rightImages;

    public void setFX(double val) {
        fX = val;
    }

    public void setFY(double val) {
        fY = val;
    }

    public double getSpeedCof(){
        return 3;
    }

    public double getVX() {
        return fX * getSpeedCof();
    }

    public double getVY() {
        return fY * getSpeedCof();
    }

    private void move(double rX, double rY) {
        setX(rX + getX());
        setY(rY + getY());

        if(getX() > world.getMapWidth())
            setX(getX() - world.getMapWidth());
        if(getX() < 0)
            setX(getX() + world.getMapWidth());
        if(getY() > world.getMapHeight())
            setY(getY() - world.getMapHeight());
        if(getY() < 0)
            setY(getY() + world.getMapHeight());
    }

    public boolean hasCollisionWithWalls(){
        return world.getWalls().stream().anyMatch(this::hasCollision);
    }

    public boolean willHaveCollisionWithWalls(){
        move(getVX(), getVY());
        boolean ret = hasCollisionWithWalls();
        move(-getVX(), -getVY());
        return ret;
    }

    public void moveConsideringWalls(){
        move(getVX(), 0);
        if(hasCollisionWithWalls())
            move(-getVX(), 0);
        move(0, getVY());
        if(hasCollisionWithWalls())
            move(0, -getVY());
    }

    abstract protected void conditionsInMovement();

    public void setWalkMovementAnimation(){
        setLoop(new KeyFrame(Duration.millis(10), e->{
            moveConsideringWalls();
            conditionsInMovement();
        }));
    }

    public void setFaceAnimation(){
        final AtomicInteger animationState = new AtomicInteger(0);
        setLoop(new KeyFrame(Duration.millis(240), e-> {
            if(getVX() == 0 && getVY() == 0) {
                setRotate(0);
                setImage(frontImages[animationState.get()]);
            } else {
                setRotate(Math.atan2(getVY(), getVX()) / Math.PI * 180);
                setImage(rightImages[animationState.get()]);
            }
            animationState.set(animationState.get() + 1);
            if(animationState.get() == frontImages.length)
                animationState.set(0);
        }));
    }

    public void activate(){
        if(rightImages == null){
            // only moves in place
            setFaceAnimation();
        } else{
            assert rightImages.length == frontImages.length;
            setFaceAnimation();
            setWalkMovementAnimation();
        }
    }

    public MovingEntity(GameWorld world, Image[] frontImages, Image[] rightImages, double x, double y){
        super(world, frontImages[0], x, y);
        this.frontImages = frontImages;
        this.rightImages = rightImages;
    }
}
