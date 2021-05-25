package org.shayan.pacman.game.entity;

import javafx.animation.KeyFrame;
import javafx.scene.image.Image;
import javafx.util.Duration;
import org.shayan.pacman.game.GameWorld;

import java.util.concurrent.atomic.AtomicInteger;

abstract public class MovingEntity extends Entity {
    protected double vX = 0, vY = 0;
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

    public boolean willHaveCollision(Entity e){
        return !(Math.abs(getCenterX() + getVX() - e.getCenterX()) >= e.getR() + getR() || Math.abs(getCenterY() + getVY() - e.getCenterY()) >= e.getR() + getR());
    }
    public boolean willHaveCollisionX(Entity e){
        return !(Math.abs(getCenterX() + getVX() - e.getCenterX()) >= e.getR() + getR() || Math.abs(getCenterY() - e.getCenterY()) >= e.getR() + getR());
    }
    public boolean willHaveCollisionY(Entity e){
        return !(Math.abs(getCenterX() - e.getCenterX()) >= e.getR() + getR() || Math.abs(getCenterY() + getVY() - e.getCenterY()) >= e.getR() + getR());
    }

    public void checkCollisionWithWall(){
        for(Wall wall : GameWorld.getInstance().getWalls()){
            if(willHaveCollision(wall)) {
                if (willHaveCollisionX(wall))
                    vX = 0;
                if (willHaveCollisionY(wall))
                    vY = 0;
            }
        }
    }

    abstract protected void conditionsInMovement();

    public void setWalkMovementAnimation(){
        setLoop(new KeyFrame(Duration.millis(10), e->{
            vX = getVX();
            vY = getVY();

            checkCollisionWithWall();
            conditionsInMovement();

            setX(vX + getX());
            setY(vY + getY());

            if(getX() > GameWorld.getInstance().getMapWidth())
                setX(0);
            if(getX() < 0)
                setX(GameWorld.getInstance().getMapWidth());
            if(getY() > GameWorld.getInstance().getMapHeight())
                setY(0);
            if(getY() < 0)
                setY(GameWorld.getInstance().getMapHeight());
        }));
    }

    public void setFaceAnimation(){
        final AtomicInteger animationState = new AtomicInteger(0);
        setLoop(new KeyFrame(Duration.millis(100), e-> {
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

    public MovingEntity(Image[] frontImages, Image[] rightImages, double x, double y){
        super(frontImages[0], x, y);
        this.frontImages = frontImages;
        this.rightImages = rightImages;
    }
}
