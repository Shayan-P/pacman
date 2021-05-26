package org.shayan.pacman.menu;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.shayan.pacman.extendedNodes.BeautifulText;

public class LoadingMenu extends AbstractMenu {
    private final BeautifulText shayan = new BeautifulText("Shayan", Color.DARKBLUE, 70);
    private final BeautifulText pardis = new BeautifulText("Pardis", Color.DARKBLUE, 75);
    private final BeautifulText presents = new BeautifulText("presents", Color.GREY, 1);

    {
        presents.setLayoutX(Width / 2 - 150);
        presents.setLayoutY(0.8 * Height);
    }

    public void addStartingTimeLine(){
        root.getChildren().addAll(shayan);

        Transition shayanMove = goTo(shayan, Width/2 - 150, Height/2 - 150);
        Transition pardisMove = goTo(pardis, Width/2, Height/2);
        Transition presentMove = getPresentsAnimation();

        shayanMove.setOnFinished(e->{
            root.getChildren().add(pardis);
            pardisMove.play();
        });
        pardisMove.setOnFinished(e->{
            root.getChildren().add(presents);
            presentMove.play();
        });
        presentMove.setOnFinished(e->{
            shayan.setEffect(new Glow(10));
            pardis.setEffect(new Glow(10));
            pauseThen(Duration.seconds(1), ()->{
                new WelcomeMenu().start(stage);
            }).play();
        });
        shayanMove.play();
    }

    public Transition getPresentsAnimation(){
        return new Transition() {
            {
                setCycleDuration(Duration.millis(2000));
            }

            @Override
            protected void interpolate(double frac) {
                presents.setFont(Font.font(1 + frac * 60));
            }
        };
    }

    public Transition pauseThen(Duration d, Runnable r){
        Transition t = new PauseTransition(d);
        t.setOnFinished(e->r.run());
        return t;
    }

    public Transition goTo(Node node, double x, double y){
        TranslateTransition t = new TranslateTransition(Duration.seconds(3), node);
        t.setToX(x);
        t.setToY(y);
        return t;
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.root = new Pane();
        this.scene = new Scene(root, Width, Height);
        stage.setScene(scene);
        addBackGround();
        addStartingTimeLine();
        stage.show();
    }
}
