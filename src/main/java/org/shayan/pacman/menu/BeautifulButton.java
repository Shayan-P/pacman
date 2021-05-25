package org.shayan.pacman.menu;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

public class BeautifulButton extends Button {
    public BeautifulButton(String text, int fontSize, Runnable onClick){
        super(text);
        setBackground(
                new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY))
        );
        setOnMouseClicked(e->onClick.run());
        effectProperty().bind(
                Bindings.when(hoverProperty())
                        .then((Effect) new DropShadow(10, Color.YELLOW))
                        .otherwise(new BoxBlur(1, 1, 3))
        );
        setTextFill(Color.WHEAT);
        setFont(Font.font(fontSize));
    }

    public BeautifulButton setNewShape(Shape shape){
        setShape(shape);
        return this;
    }
}
