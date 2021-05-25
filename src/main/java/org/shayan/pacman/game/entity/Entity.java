package org.shayan.pacman.game.entity;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.shayan.pacman.database.Settings;
import org.shayan.pacman.game.GameWorld;
import org.shayan.pacman.menu.GameMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

abstract public class Entity extends ImageView {
    abstract public double getR();
    private final List<Timeline> allTimelines = new ArrayList<>();

    public double getCenterX(){
        return getX() + getR();
    }
    public double getCenterY(){
        return getY() + getR();
    }

    public double getManhattanDistance(Entity e){
        return Math.abs(getCenterX() - e.getCenterX()) + Math.abs(getCenterY() - e.getCenterY());
    }

    public boolean hasCollision(Entity e){
        return !(Math.abs(getCenterX() - e.getCenterX()) >= e.getR() + getR() || Math.abs(getCenterY() - e.getCenterY()) >= e.getR() + getR());
    }

    protected static Image getImageInResource(String path){
        return new Image(Entity.class.getResource(path).toExternalForm());
    }

    public void setLoop(KeyFrame keyFrame){
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        allTimelines.add(timeline);
        GameMenu.getInstance().addGameLoop(timeline);
    }

    public void stopAllTimelines(){
        allTimelines.forEach(Animation::stop);
    }
    public void playAllTimelines(){
        allTimelines.forEach(Animation::play);
    }

    public void setImageSize(){
        setFitWidth(getR() * 2);
        setFitHeight(getR() * 2);
    }

    public void setInitialPosition(double x, double y){
        setX(x);
        setY(y);
    }

    public Entity(Image initImage, double x, double y){
        super(initImage);
        setImageSize();
        setInitialPosition(x, y);
    }
}
