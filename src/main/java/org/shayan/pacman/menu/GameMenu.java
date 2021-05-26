package org.shayan.pacman.menu;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.shayan.pacman.database.MapDatabase;
import org.shayan.pacman.database.Settings;
import org.shayan.pacman.game.GameWorld;
import org.shayan.pacman.game.event.*;

import java.util.ArrayList;
import java.util.List;

public class GameMenu extends AbstractMenu {
    private Stage stage;
    private Scene scene;
    private Pane root;
    private HBox navBar;
    private Pane gameBar;
    private ShapeBar lifeBar;
    private CounterBar coinBar;
    private CounterBar highScoreBar;
    private final List<Timeline> gameTimelines = new ArrayList<>();
    private final PlayPauseIcon playPauseIcon = new PlayPauseIcon();
    private final ExitIcon exitIcon = new ExitIcon();
    private static GameMenu instance;
    private boolean gameOverLock = true; // this is a lock for race condition

    public static GameMenu getInstance(){
        return instance;
    }

    public GameMenu(){
        super();
        instance = this;
    }

    public void startNewGame(){
        gameOverLock = false;
        lifeBar.refresh();
        GameWorld.getInstance().startNewRound();
        coinBar.refresh();
        MediaPlayer.play(MediaPlayer.SoundType.START);
        PauseTransition pt = new PauseTransition(Duration.seconds(5));
        pt.setOnFinished(e->gameTimelines.forEach(Animation::play));
        pt.play();
    }
    public void endOfTheGame(){
        if(gameOverLock)
            return;
        gameOverLock = true;
        gameTimelines.forEach(Animation::stop);
        if(GameWorld.getInstance().getLives() <= 0) {
            BorderPane pane = new BorderPane();
            pane.setLayoutX(Width/2 - 100);
            pane.setLayoutY(Height/2 - 100);
            gameBar.getChildren().add(pane);

            // instance = null is for clearing the future access
            new AlertBox().display(pane, "Continue?",
                    ()->{ new GameMenu().start(stage); },
                    ()->{ instance = null; new WelcomeMenu().start(stage); }
                    );
        } else {
            PauseTransition pt = new PauseTransition(Duration.seconds(3));
            pt.setOnFinished(e -> startNewGame());
            pt.play();
        }
    }

    private void addControllers(){
        scene.setOnKeyPressed(e->{
            if(e.getCode().equals(KeyCode.LEFT))
                GameWorld.getInstance().getPacman().setFX(-1);
            if(e.getCode().equals(KeyCode.RIGHT))
                GameWorld.getInstance().getPacman().setFX(1);
            if(e.getCode().equals(KeyCode.UP))
                GameWorld.getInstance().getPacman().setFY(-1);
            if(e.getCode().equals(KeyCode.DOWN))
                GameWorld.getInstance().getPacman().setFY(1);
        });
        scene.setOnKeyReleased(e->{
            if(e.getCode().equals(KeyCode.LEFT))
                GameWorld.getInstance().getPacman().setFX(0);
            if(e.getCode().equals(KeyCode.RIGHT))
                GameWorld.getInstance().getPacman().setFX(0);
            if(e.getCode().equals(KeyCode.UP))
                GameWorld.getInstance().getPacman().setFY(0);
            if(e.getCode().equals(KeyCode.DOWN))
                GameWorld.getInstance().getPacman().setFY(0);
        });
    }
    public void addBackGroundColor(){
        Stop[] stops = new Stop[] {
                new Stop(0, Color.DARKSLATEBLUE),
                new Stop(1, Color.DARKRED)
        };
        LinearGradient gradient =
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        gameBar.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        navBar.setBackground(new Background(new BackgroundFill(Color.ROSYBROWN, CornerRadii.EMPTY, Insets.EMPTY)));
        navBar.setBorder(new Border(new BorderStroke(Color.BROWN, BorderStrokeStyle.SOLID, new CornerRadii(1), new BorderWidths(3), Insets.EMPTY)));
    }

    public void addGameLoop(Timeline timeline){
        gameTimelines.add(timeline);
    }

    public void addNavBarItems(){
        lifeBar = new ShapeBar("/icons/heart.png") {
            @Override
            int getNumber() {
                return GameWorld.getInstance().getLives();
            }
        };
        coinBar = new CounterBar("/icons/coin.png") {
            @Override
            int getNumber() {
                return GameWorld.getInstance().getEatenCoins();
            }
        };
        highScoreBar = new CounterBar(new BeautifulText("Highest Score:", Color.BLACK, 27)) {
            @Override
            int getNumber() {
                return GameWorld.getInstance().getHighestScore();
            }
        };
        playPauseIcon.setOnMouseClicked(e-> {
//            MediaPlayer.play(MediaPlayer.SoundType.TICK);
                    playPauseIcon.toggle();
                    if (playPauseIcon.isPlay())
                        gameTimelines.forEach(Animation::play);
                    else
                        gameTimelines.forEach(Animation::stop);
                });

        exitIcon.setOnMouseClicked(e->{
            if(playPauseIcon.isPlay()) {
                playPauseIcon.toggle();
                gameTimelines.forEach(Animation::stop);
            }
            new WelcomeMenu().start(stage);
        });

        navBar.setSpacing(50);
        navBar.getChildren().addAll(lifeBar, coinBar, highScoreBar, playPauseIcon, exitIcon);
    }

    private void addCustomEventHandlers(){
        this.gameBar.addEventHandler(GhostEatEvent.MY_TYPE, e->{
            GameWorld.getInstance().pacmanAndAIIntersect(e.getAI());
        });
        this.gameBar.addEventHandler(CoinEatEvent.MY_TYPE, e->{
            MediaPlayer.play(MediaPlayer.SoundType.COIN);
            GameWorld.getInstance().pacmanEatsCoin(e.getCoin());
            coinBar.refresh();
            highScoreBar.refresh();
        });
        this.gameBar.addEventHandler(EndOfRoundEvent.MY_TYPE, e->{
            MediaPlayer.play(MediaPlayer.SoundType.DEATH);
            lifeBar.refresh();
            endOfTheGame();
        });
        this.gameBar.addEventHandler(PacmanWinsEvent.MY_TYPE, e->{
            lifeBar.refresh();
            endOfTheGame();
        });
    }
    public void fireEvent(Event e){
        this.gameBar.fireEvent(e);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setScene(this.scene = new Scene(this.root = new VBox(), Width, Height));
        this.root.getChildren().addAll(this.navBar = new HBox(), this.gameBar = new Pane());
        new GameWorld(this.gameBar);
        addBackGroundColor();
        addNavBarItems();
        addControllers();
        addCustomEventHandlers();
        startNewGame();
        stage.show();
    }

    public void resumeStart(Stage stage){
        this.stage = stage;
        stage.setScene(this.scene);
    }
}
