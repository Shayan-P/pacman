package org.shayan.pacman.game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.Pair;
import org.shayan.pacman.database.Settings;
import org.shayan.pacman.game.entity.*;
import org.shayan.pacman.game.entity.ai.*;
import org.shayan.pacman.game.event.EndOfRoundEvent;
import org.shayan.pacman.game.event.PacmanWinsEvent;
import org.shayan.pacman.model.GameMap;
import org.shayan.pacman.model.User;

import java.io.*;
import java.util.*;

public class GameWorld {
    private final List<Wall> walls = new ArrayList<>();
    private final List<AI> ais = new ArrayList<>();
    private final List<Coin> coins = new ArrayList<>();
    private final double mapWidth;
    private final double mapHeight;
    private final Pane root;
    private final double BLOCK_LENGTH;
    private final GameMap gameMap;

    private Pacman pacman;
    private int eatenCoins;
    private int currentScore;
    private int lives = Settings.getDefaultPacmanHearts();
    private int highestScore;
    private boolean isFirstRound;
    private final List<Timeline> gameTimelines = new ArrayList<>();
    private final List<Timeline> waitingTasks = new ArrayList<>();

    {
        isFirstRound = true;
        highestScore = User.getCurrentUser() == null ? 0 : User.getCurrentUser().getScore();
        eatenCoins = 0;
        currentScore = 0;
    }

    public GameWorld(Pane root){
        this.root = root;

        try {
            gameMap = new GameMap(new FileReader(Settings.getDefaultMapPath()));
        } catch (Exception e){
            throw new Error("error in reading map");
        }
        BLOCK_LENGTH = 960.0 / Math.max(gameMap.getWidth(), gameMap.getHeight());

        mapWidth = gameMap.getWidth() * BLOCK_LENGTH;
        mapHeight = gameMap.getHeight() * BLOCK_LENGTH;

        loadWalls();
        loadCoins();
        loadPacmanAndAIs();
    }

    public void finishRemainingTasks(){
        while (!waitingTasks.isEmpty()){
            Timeline tt = waitingTasks.get(0);
            tt.stop();
            tt.getOnFinished().handle(null);
        }
    }

    public void startNewRound(){
        if(isFirstRound){
            isFirstRound = false;
            return;
        }
        finishRemainingTasks();
        restartPacmanAndAIs();
        if (coins.isEmpty())
            loadCoins();
    }

    public void pacmanEatsCoin(Coin coin){
        coins.remove(coin);
        root.getChildren().remove(coin);
        if(coin instanceof SpecialCoin) {
            pacman.eatsSuperCoin();
        }
        else {
            eatenCoins += 1;
            addScore(5);
        }
        highestScore = Math.max(highestScore, currentScore);
        if(User.getCurrentUser() != null)
            User.getCurrentUser().updateScore(currentScore);
        if(coins.isEmpty()){
            lives += 1;
            fireEvent(new PacmanWinsEvent());
        }
    }
    public void pacmanAndAIIntersect(AI ai){
        if(pacman.isSuperManMode()){
            addScore(200);
            spawnAIRandomPlace(ai);
        } else {
            lives -= 1;
            fireEvent(new EndOfRoundEvent());
        }
    }

    public void addScore(int delta){
        currentScore += delta;
        highestScore = Math.max(highestScore, currentScore);
        if(User.getCurrentUser() != null)
            User.getCurrentUser().updateScore(currentScore);
    }

    private void loadPacmanAndAIs() {
        Pair<Double, Double> pacmanPos = getPacmanSpawnPosition();
        List<Pair<Double, Double>> aisPos = getAISpawnPositions();
        assert pacmanPos != null;
        this.pacman = new Pacman(this, pacmanPos.getKey(), pacmanPos.getValue());

        ais.add(new BfsAI(this, 700, 0, aisPos.get(0).getKey(), aisPos.get(0).getValue()));
        if (Settings.getGameDifficulty() == 0) {
            ais.add(new RandomAI(this, 1, aisPos.get(1).getKey(), aisPos.get(1).getValue()));
            ais.add(new LineChaserAI(this, 2, aisPos.get(2).getKey(), aisPos.get(2).getValue()));
            ais.add(new NegativeAI(this, 3, aisPos.get(3).getKey(), aisPos.get(3).getValue()));
        } else if (Settings.getGameDifficulty() == 1) {
            ais.add(new BfsAI(this, 500, 1, aisPos.get(1).getKey(), aisPos.get(1).getValue()));
            ais.add(new LineChaserAI(this, 2, aisPos.get(2).getKey(), aisPos.get(2).getValue()));
            ais.add(new NegativeAI(this, 3, aisPos.get(3).getKey(), aisPos.get(3).getValue()));
        } else if (Settings.getGameDifficulty() == 2) {
            ais.add(new BfsAI(this, 1000, 1, aisPos.get(1).getKey(), aisPos.get(1).getValue()));
            ais.add(new BfsAI(this, 900, 2, aisPos.get(2).getKey(), aisPos.get(2).getValue()));
            ais.add(new BfsAI(this, 500, 3, aisPos.get(3).getKey(), aisPos.get(3).getValue()));
        }

        this.pacman.activate();
        ais.forEach(MovingEntity::activate);
        root.getChildren().add(pacman);
        root.getChildren().addAll(ais);
    }

    private List<Pair<Double, Double>> getAISpawnPositions() {
        List<Pair<Double, Double>> ret = new ArrayList<>();
        for (int i = 0; i < gameMap.getWidth(); i++) {
            for (int j = 0; j < gameMap.getHeight(); j++) {
                MapEntity cell = gameMap.getCells()[i][j];
                double x = BLOCK_LENGTH * i, y = BLOCK_LENGTH * j;
                if (cell.equals(MapEntity.AI)) {
                    ret.add(new Pair<>(x, y));
                }
            }
        }
        return ret;
    }

    private Pair<Double, Double> getPacmanSpawnPosition() {
        for (int i = 0; i < gameMap.getWidth(); i++) {
            for (int j = 0; j < gameMap.getHeight(); j++) {
                MapEntity cell = gameMap.getCells()[i][j];
                double x = BLOCK_LENGTH * i, y = BLOCK_LENGTH * j;
                if (cell.equals(MapEntity.PACMAN)) {
                    return new Pair<>(x, y);
                }
            }
        }
        return null;
    }

    private void spawnAIRandomPlace(AI ai){
        List<Pair<Double, Double>> poses = getAISpawnPositions();
        Pair<Double, Double> pos = poses.get(new Random().nextInt(poses.size()));
        restartMovingEntity(ai, pos.getKey(), pos.getValue(), Duration.seconds(6));
    }

    private void restartMovingEntity(MovingEntity entity, double x, double y, Duration wait){
        entity.stopAllTimelines();
        entity.setX(x);
        entity.setY(y);
        if(wait != null)
            addWaitingTask(wait, entity::playAllTimelines);
    }

    private void restartPacmanAndAIs(){
        int aiCounter = 0;
        for(int i = 0; i < gameMap.getWidth(); i++){
            for(int j = 0; j < gameMap.getHeight(); j++){
                MapEntity cell = gameMap.getCells()[i][j];
                double x = BLOCK_LENGTH * i, y = BLOCK_LENGTH * j;
                if(cell.equals(MapEntity.PACMAN)) {
                    restartMovingEntity(this.pacman, x, y, null);
                }
                if(cell.equals(MapEntity.AI)){
                    restartMovingEntity(ais.get(aiCounter), x, y, null);
                    aiCounter += 1;
                }
            }
        }
    }

    private void loadCoins() {
        for (int i = 0; i < gameMap.getWidth(); i++) {
            for (int j = 0; j < gameMap.getHeight(); j++) {
                MapEntity cell = gameMap.getCells()[i][j];
                double x = BLOCK_LENGTH * i, y = BLOCK_LENGTH * j;
                if (cell.equals(MapEntity.COIN)) {
                    Coin coin = new Coin(this, x, y);
                    coin.activate();
                    root.getChildren().add(coin);
                    coins.add(coin);
                }
                if (cell.equals(MapEntity.SPECIAL_COIN)) {
                    SpecialCoin coin = new SpecialCoin(this, x, y);
                    coin.activate();
                    root.getChildren().add(coin);
                    coins.add(coin);
                }
            }
        }
    }
    private void loadWalls(){
        for (int i = 0; i < gameMap.getWidth(); i++) {
            for (int j = 0; j < gameMap.getHeight(); j++) {
                MapEntity cell = gameMap.getCells()[i][j];
                double x = BLOCK_LENGTH * i, y = BLOCK_LENGTH * j;
                if(cell.equals(MapEntity.WALL)){
                    Wall wall = new Wall(this, x, y);
                    root.getChildren().add(wall);
                    walls.add(wall);
                }
            }
        }
    }

    public Pacman getPacman() {
        return pacman;
    }

    public double getMapWidth() {
        return mapWidth;
    }

    public double getMapHeight() {
        return mapHeight;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<AI> getAIs() {
        return ais;
    }

    public List<Coin> getCoins() {
        List<Coin> other = new ArrayList<>();
        other.addAll(coins);
        return other;
    }

    public int getLives() {
        return lives;
    }

    public int getEatenCoins() {
        return eatenCoins;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getCurrentScore(){
        return currentScore;
    }

    public double getBlockLength() {
        return BLOCK_LENGTH;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void fireEvent(Event e){
        this.root.fireEvent(e);
    }

    public void addGameLoop(Timeline timeline){
        gameTimelines.add(timeline);
    }
    public void addWaitingTask(Duration duration, Runnable runnable){
        Timeline tt = new Timeline(new KeyFrame(duration, e->{}));
        gameTimelines.add(tt);
        waitingTasks.add(tt);
        tt.setOnFinished(e-> {
            gameTimelines.remove(tt);
            waitingTasks.remove(tt);
            runnable.run();
        });
        tt.setCycleCount(1);
        tt.play();
    }

    public void  stopMoving(){
        gameTimelines.forEach(Animation::stop);
        // todo changed to pause
    }
    public void startMoving(){
        gameTimelines.forEach(Animation::play);
    }
}
