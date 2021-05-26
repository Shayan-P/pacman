package org.shayan.pacman.menu;

import javafx.beans.binding.Bindings;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class ExitIcon extends StackPane {
    public ExitIcon(){
        ImageView view = new ImageView();
        view.setImage(new Image(getClass().getResource("/icons/exit.png").toExternalForm()));

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
