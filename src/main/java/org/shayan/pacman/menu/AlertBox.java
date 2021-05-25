package org.shayan.pacman.menu;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class AlertBox {
    public static Stage stageGenerator(String title, double minWidth, double minHeight){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);
        return stage;
    }

    public boolean display(String question){
        Stage stage = stageGenerator("alert", 100, 100);
        AtomicBoolean ret = new AtomicBoolean();
        BorderPane root = new BorderPane();
        display(root, question, ()->{ret.set(true); stage.close();}, ()->{ret.set(false); stage.close();});
        stage.setScene(new Scene(root));
        stage.showAndWait();
        return ret.get();
    }

    public void display(BorderPane root, String question, Runnable yesRun, Runnable noRun){
        Font font = Font.font("Courier New", FontWeight.BOLD, 20);

        Button yes = new Button("yes");
        Button no = new Button("no");

        yes.setOnMouseClicked(e->{ yesRun.run(); });
        yes.setFont(font);
        yes.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));

        no.setOnMouseClicked(e->{ noRun.run(); });
        no.setFont(font);
        no.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

        root.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setBottom(new HBox(yes, no));
        Label label = new Label(question);
        label.setFont(Font.font(15));
        root.setTop(label);
    }
}
