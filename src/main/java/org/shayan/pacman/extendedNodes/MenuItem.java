package org.shayan.pacman.extendedNodes;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MenuItem extends Pane {
    MenuItem(String name){
        Text text = new Text(name);
        text.setFill(Color.WHITE);
        text.setFont(Font.font(30));
        text.setTranslateY(10);
        ReadOnlyBooleanProperty property = text.hoverProperty();
        text.effectProperty().bind(
                Bindings.when(property)
                        .then((Effect) new DropShadow(10, Color.YELLOW))
                        .otherwise(new BoxBlur(1, 1, 3))
        );
        Polygon poly = new Polygon(
                -50, -50,
                -20, 0,
                10, -50,
                -20, 50
        );
        poly.setEffect(new BoxBlur(2, 1, 3));
        poly.fillProperty().bind(
                Bindings.when(property)
                .then(Color.BLACK)
                .otherwise(Color.color(1, 0.7, 0.3, 1))
        );
        getChildren().addAll(poly, text);
    }
    public MenuItem(String name, Runnable action) {
        this(name);
        setOnClick(action);
    }

    public void setOnClick(Runnable action){
        setOnMouseClicked(e -> action.run());
    }

    public MenuItem setX(double x){
        setLayoutX(x);
        return this;
    }
    public MenuItem setY(double y){
        setLayoutY(y);
        return this;
    }
}
