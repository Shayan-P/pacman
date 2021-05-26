package org.shayan.pacman.game.entity;

import javafx.scene.image.Image;
import org.shayan.pacman.database.Settings;
import org.shayan.pacman.game.GameWorld;
import org.shayan.pacman.game.event.CoinEatEvent;
import org.shayan.pacman.menu.GameMenu;

public class Pacman extends MovingEntity {
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
}
