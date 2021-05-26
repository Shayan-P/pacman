package org.shayan.pacman.menu;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.shayan.pacman.database.History;
import org.shayan.pacman.game.GameWorld;
import org.shayan.pacman.game.event.*;

public class GameMenu extends AbstractMenu {
    private Stage stage;
    private Scene scene;
    private Pane root;
    private GameWorld gameWorld;
    private HBox navBar;
    private Pane gameBar;
    private ShapeBar lifeBar;
    private CounterBar coinBar;
    private CounterBar highScoreBar;
    private final PlayPauseIcon playPauseIcon = new PlayPauseIcon();
    private final ExitIcon exitIcon = new ExitIcon();
    private boolean gameOverLock = true; // this is a lock for race condition
    private boolean finalFinished = false;

    public GameMenu(){
        super();
        History.setLastGameMenu(this);
    }

    public void startNewGame(){
        gameOverLock = false;
        lifeBar.refresh();
        gameWorld.startNewRound();
        coinBar.refresh();
        MediaPlayer.play(MediaPlayer.SoundType.START);
        PauseTransition pt = new PauseTransition(Duration.seconds(5));
        pt.setOnFinished(e->gameWorld.startMoving());
        pt.play();
    }
    public void endOfTheGame(){
        if(gameOverLock)
            return;
        gameOverLock = true;
        gameWorld.stopMoving();
        if(gameWorld.getLives() <= 0) {
            BorderPane pane = new BorderPane();
            pane.setLayoutX(Width/2 - 100);
            pane.setLayoutY(Height/2 - 100);
            gameBar.getChildren().add(pane);

            finalFinished = true;

            new AlertBox().display(
                    pane,
                    "Continue?",
                    ()->new GameMenu().start(stage),
                    ()->new WelcomeMenu().start(stage)
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
                gameWorld.getPacman().setFX(-1);
            if(e.getCode().equals(KeyCode.RIGHT))
                gameWorld.getPacman().setFX(1);
            if(e.getCode().equals(KeyCode.UP))
                gameWorld.getPacman().setFY(-1);
            if(e.getCode().equals(KeyCode.DOWN))
                gameWorld.getPacman().setFY(1);
        });
        scene.setOnKeyReleased(e->{
            if(e.getCode().equals(KeyCode.LEFT))
                gameWorld.getPacman().setFX(0);
            if(e.getCode().equals(KeyCode.RIGHT))
                gameWorld.getPacman().setFX(0);
            if(e.getCode().equals(KeyCode.UP))
                gameWorld.getPacman().setFY(0);
            if(e.getCode().equals(KeyCode.DOWN))
                gameWorld.getPacman().setFY(0);
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

    public void addNavBarItems(){
        lifeBar = new ShapeBar("/icons/heart.png") {
            @Override
            int getNumber() {
                return gameWorld.getLives();
            }
        };
        coinBar = new CounterBar("/icons/coin.png") {
            @Override
            int getNumber() {
                return gameWorld.getEatenCoins();
            }
        };
        highScoreBar = new CounterBar(new BeautifulText("Highest Score:", Color.BLACK, 27)) {
            @Override
            int getNumber() {
                return gameWorld.getHighestScore();
            }
        };
        playPauseIcon.setOnMouseClicked(e-> {
//            MediaPlayer.play(MediaPlayer.SoundType.TICK);
                    playPauseIcon.toggle();
                    if (playPauseIcon.isPlay())
                        gameWorld.startMoving();
                    else
                        gameWorld.stopMoving();
                });

        exitIcon.setOnMouseClicked(e->{
            if(playPauseIcon.isPlay()) {
                playPauseIcon.toggle();
                gameWorld.stopMoving();
            }
            new WelcomeMenu().start(stage);
        });

        navBar.setSpacing(50);
        navBar.getChildren().addAll(lifeBar, coinBar, highScoreBar, playPauseIcon, exitIcon);
    }

    private void addCustomEventHandlers(){
        this.gameBar.addEventHandler(GhostEatEvent.MY_TYPE, e->{
            gameWorld.pacmanAndAIIntersect(e.getAI());
        });
        this.gameBar.addEventHandler(CoinEatEvent.MY_TYPE, e->{
            MediaPlayer.play(MediaPlayer.SoundType.COIN);
            gameWorld.pacmanEatsCoin(e.getCoin());
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

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setScene(this.scene = new Scene(this.root = new VBox(), Width, Height));
        this.root.getChildren().addAll(this.navBar = new HBox(), this.gameBar = new Pane());
        gameWorld = new GameWorld(this.gameBar);
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

    public boolean isFinalFinished(){
        return finalFinished;
    }
}
