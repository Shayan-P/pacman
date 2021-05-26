package org.shayan.pacman.game.entity;

import javafx.scene.image.Image;
import javafx.util.Duration;
import org.shayan.pacman.database.Settings;
import org.shayan.pacman.game.GameWorld;
import org.shayan.pacman.game.event.CoinEatEvent;
import org.shayan.pacman.game.event.PacmanSuperManEvent;

public class Pacman extends MovingEntity {
    private int activeSuperCoins = 0;

    @Override
    public double getR() {
        return 0.8 * world.getBlockLength() / 2;
    }

    private void collisionWithCoin(){
        for(Coin coin: world.getCoins()){
            if(hasCollision(coin)) {
                world.fireEvent(new CoinEatEvent(coin));
            }
        }
    }

    @Override
    protected void conditionsInMovement() {
        collisionWithCoin();
    }

    private static Image[] getFrontImages(){
        Image[] frontImages = new Image[4];
        for(int i = 0; i < 4; i++) {
            frontImages[i] = getImageInResource(String.format("%s/front/%d.png", Settings.getFavoritePacmanPathPrefix(), i));
        }
        return frontImages;
    }
    private static Image[] getRightImages(){
        Image[] rightImages = new Image[4];
        for(int i = 0; i < 4; i++) {
            rightImages[i] = getImageInResource(String.format("%s/right/%d.png", Settings.getFavoritePacmanPathPrefix(), i));
        }
        return rightImages;
    }

    public Pacman(GameWorld world, double x, double y) {
        super(world, getFrontImages(), getRightImages(), x, y);
    }

    public void eatsSuperCoin(){
        activeSuperCoins += 1;
        world.fireEvent(new PacmanSuperManEvent(true));
        world.addWaitingTask(Duration.seconds(10), ()->{
            activeSuperCoins -= 1;
            if(activeSuperCoins == 0)
                world.fireEvent(new PacmanSuperManEvent(false));
        });
    }

    public boolean isSuperManMode(){
        return activeSuperCoins > 0;
    }
}
