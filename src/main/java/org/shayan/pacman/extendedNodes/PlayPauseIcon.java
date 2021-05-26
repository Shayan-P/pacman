package org.shayan.pacman.extendedNodes;

import javafx.beans.binding.Bindings;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class PlayPauseIcon extends StackPane {
    private static final Image pauseImage = new Image(PlayPauseIcon.class.getResource("/icons/pause.png").toExternalForm());
    private static final Image playImage = new Image(PlayPauseIcon.class.getResource("/icons/play.png").toExternalForm());
    private boolean play;
    private ImageView view;

    public boolean isPlay(){
        return play;
    }
    public void toggle(){
        if(play){
            play = false;
            view.setImage(playImage);
        } else{
            play = true;
            view.setImage(pauseImage);
        }
    }

    public PlayPauseIcon(){
        play = true;
        view = new ImageView(pauseImage);

        view.fitHeightProperty().bind(
                Bindings.when(hoverProperty())
                .then(40)
                .otherwise(35)
        );
        view.fitWidthProperty().bind(
                Bindings.when(hoverProperty())
                        .then(40)
                        .otherwise(35)
        );
        getChildren().add(view);
    }
}
