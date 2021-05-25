package org.shayan.pacman.menu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayPauseIcon extends ImageView {
    private static final Image pauseImage = new Image(PlayPauseIcon.class.getResource("/icons/pause.png").toExternalForm());
    private static final Image playImage = new Image(PlayPauseIcon.class.getResource("/icons/play.png").toExternalForm());
    private boolean play;

    public boolean isPlay(){
        return play;
    }
    public void toggle(){
        if(play){
            play = false;
            setImage(playImage);
        } else{
            play = true;
            setImage(pauseImage);
        }
    }

    public PlayPauseIcon(){
        play = true;
        setImage(pauseImage);
        setFitHeight(35);
        setFitWidth(35);
    }
}
