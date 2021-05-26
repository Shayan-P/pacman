package org.shayan.pacman.extra;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.shayan.pacman.game.entity.Pacman;

public class DummyPacman extends Pacman {
    double destinationX = 100;
    double destinationY = 100;

    public DummyPacman(double x, double y) {
        super(null, x, y);
    }

    @Override
    public double getSpeedCof(){
        return 0.8;
    }

    @Override
    public double getR(){
        return 30;
    }

    @Override
    public void setWalkMovementAnimation(){
        setLoop(new KeyFrame(Duration.millis(10), e->{
            double dirX = destinationX - getX();
            double dirY = destinationY - getY();
            double dirLen = Math.sqrt(dirX * dirX + dirY * dirY);
            if(dirLen < 3){
                setFX(0);
                setFY(0);
            } else {
                setFX(dirX / dirLen * 2);
                setFY(dirY / dirLen * 2);
            }
            setX(getVX() + getX());
            setY(getVY() + getY());
        }));
    }

    @Override
    public void setLoop(KeyFrame keyFrame){
        Timeline tt = new Timeline(keyFrame);
        tt.setCycleCount(Timeline.INDEFINITE);
        tt.play();
    }

    public void setDestination(double x, double y){
        destinationX = x;
        destinationY = y;
    }
}