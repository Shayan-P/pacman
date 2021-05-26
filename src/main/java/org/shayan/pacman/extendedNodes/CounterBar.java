package org.shayan.pacman.extendedNodes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

abstract public class CounterBar extends HBox {
    protected Image image;
    protected BeautifulText text;

    private void setShape(String path){
        image = new Image(getClass().getResource(path).toExternalForm());
    }
    public abstract int getNumber();
    public void refresh(){
        setSpacing(2);
        getChildren().clear();
        BeautifulText number = new BeautifulText(Integer.toString(getNumber()), Color.BLACK, 27);
        if(image != null)
            getChildren().addAll(number, new ImageView(image));
        else
            getChildren().addAll(text, number);
    }
    public CounterBar(String path){
        setShape(path);
        refresh();
    }
    public CounterBar(BeautifulText text){
        this.text = text;
        refresh();
    }
}
