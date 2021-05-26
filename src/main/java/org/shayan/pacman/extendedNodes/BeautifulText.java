package org.shayan.pacman.extendedNodes;

import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BeautifulText extends Text {
    public BeautifulText(String text, Paint paint, int size){
        super(text);
        setFont(Font.font(size));
        setFill(paint);
    }
}
