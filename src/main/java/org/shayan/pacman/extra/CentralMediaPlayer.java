package org.shayan.pacman.extra;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import org.shayan.pacman.App;
import org.shayan.pacman.database.History;
import org.shayan.pacman.database.Settings;
import javafx.scene.media.MediaPlayer;
import org.shayan.pacman.menu.GameMenu;

public class CentralMediaPlayer {
    private static final AudioClip coinEating = new AudioClip(CentralMediaPlayer.class.getResource("/audio/coin-eating.wav").toExternalForm());
    private static final AudioClip death = new AudioClip(CentralMediaPlayer.class.getResource("/audio/death.wav").toExternalForm());
    private static final AudioClip startOfGame = new AudioClip(CentralMediaPlayer.class.getResource("/audio/start-of-game.wav").toExternalForm());
    private static final AudioClip win = new AudioClip(CentralMediaPlayer.class.getResource("/audio/win.wav").toExternalForm());
    private static final MediaPlayer background = new MediaPlayer(new Media(CentralMediaPlayer.class.getResource("/audio/background.wav").toExternalForm()));
    private static final SimpleBooleanProperty weAreInGamePlay = new SimpleBooleanProperty(false);

    static {
        App.primaryStage.sceneProperty().addListener(e->{
            if(History.getLastGameMenu() == null)
                weAreInGamePlay.set(false);
            else
                weAreInGamePlay.set(App.primaryStage.getScene().equals(History.getLastGameMenu().getScene()));
        });

        background.muteProperty().bind(Settings.getSoundIsOnProperty().and(weAreInGamePlay.not()).not());
        background.setAutoPlay(true);
        background.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public enum SoundType{
        COIN(coinEating), DEATH(death), START(startOfGame), WIN(win);
        private final AudioClip clip;
        SoundType(AudioClip clip){
            this.clip = clip;
        }
        protected AudioClip getClip(){
            return clip;
        }
    }

    public static boolean isPlaying(){
        return coinEating.isPlaying() || death.isPlaying() || startOfGame.isPlaying() || win.isPlaying();
    }
    private static void stopAll(){
        coinEating.stop();
        death.stop();
        startOfGame.stop();
        win.stop();
    }
    public static void play(SoundType type){
        if(Settings.isSoundOn()) {
            stopAll();
            type.getClip().play();
        }
    }

    public static void playBackgroundMusic(){
        background.play();
    }
}
