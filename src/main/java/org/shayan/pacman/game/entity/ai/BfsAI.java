package org.shayan.pacman.game.entity.ai;

import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.util.Pair;
import org.shayan.pacman.game.GameWorld;
import org.shayan.pacman.game.MapEntity;
import org.shayan.pacman.game.entity.Wall;
import org.shayan.pacman.model.GameMap;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class BfsAI extends AI {
    private static final int[] dx = new int[] {1, -1, 0, 0, 1, 1, -1, -1};
    private static final int[] dy = new int[] {0, 0, 1, -1, 1, -1, 1, -1};
    private static final int dirs = 8;

    private Integer[][] bfsDad;
    private final double bfsInterval;

    private static Random rnd = new Random();

    private static int[] getRandomPermutation(){
        int[] arr = new int[dirs];
        for(int i = 0; i < dirs; i++)
            arr[i] = i;
        for(int i = 0; i < 30; i++){
            int x = rnd.nextInt(dirs), y = rnd.nextInt(dirs);
            int z = arr[x];
            arr[x] = arr[y];
            arr[y] = z;
        }
        return arr;
    }

    @Override
    void setStrategy() {
        setLoop(new KeyFrame(Duration.millis(bfsInterval), e->{
            GameWorld world = GameWorld.getInstance();
            double pacX = world.getPacman().getCenterX();
            double pacY = world.getPacman().getCenterY();

            int n = world.getGameMap().getWidth();
            int m = world.getGameMap().getHeight();
            int srcX = (int) (pacX / world.getBlockLength());
            int srcY = (int) (pacY / world.getBlockLength());

            srcX = Math.max(0, Math.min(srcX, n-1));
            srcY = Math.max(0, Math.min(srcY, m-1));

            Integer[][] copyBfsDad = new Integer[n][m];
            Queue<Pair<Integer, Integer>> qu = new LinkedList<>();
            copyBfsDad[srcX][srcY] = 0;

            qu.add(new Pair<>(srcX, srcY));
            while(!qu.isEmpty()) {
                Pair<Integer, Integer> pt = qu.poll();
                for(int dirId : getRandomPermutation()){
                    int X = pt.getKey() + dx[dirId], Y = pt.getValue() + dy[dirId];
                    if(X < 0 || Y < 0 || X >= n || Y >= m)
                        continue;
                    if(copyBfsDad[X][Y] != null)
                        continue;
                    if(world.getGameMap().getCells()[X][Y] == MapEntity.WALL)
                        continue;
                    if(dirId >= 4 && world.getGameMap().getCells()[X][Y - dy[dirId]] == MapEntity.WALL)
                        continue;
                    if(dirId >= 4 && world.getGameMap().getCells()[X - dx[dirId]][Y] == MapEntity.WALL)
                        continue;
                    copyBfsDad[X][Y] = dirId;
                    qu.add(new Pair<>(X, Y));
                }
            }
            bfsDad = copyBfsDad;
        }));
        setLoop(new KeyFrame(Duration.millis(50), e->{
            double pacX = GameWorld.getInstance().getPacman().getCenterX();
            double pacY = GameWorld.getInstance().getPacman().getCenterY();
            double dirX = pacX - getCenterX(), dirY = pacY - getCenterY();

            int nowX = (int) (getX() / GameWorld.getInstance().getBlockLength());
            int nowY = (int) (getY() / GameWorld.getInstance().getBlockLength());
            nowX = Math.max(0, Math.min(nowX, GameWorld.getInstance().getGameMap().getWidth()-1));
            nowY = Math.max(0, Math.min(nowY, GameWorld.getInstance().getGameMap().getHeight()-1));

            if(bfsDad == null || bfsDad[nowX][nowY] == null || Math.sqrt(dirX * dirX + dirY * dirY) <= GameWorld.getInstance().getBlockLength()){
                setDirection(dirX, dirY);
            } else{
                double nowXR = (nowX + 0.5) * GameWorld.getInstance().getBlockLength();
                double nowYR = (nowY + 0.5) * GameWorld.getInstance().getBlockLength();
                double dirXR = nowXR - getCenterX();
                double dirYR = nowYR - getCenterY();
                setDirection(-dx[bfsDad[nowX][nowY]], -dy[bfsDad[nowX][nowY]]);

                GameMap mm = GameWorld.getInstance().getGameMap();
                boolean collision = false;
                for(int i = nowX - 2; i <= nowX + 2; i++){
                    for(int j = nowY - 2; j <= nowY + 2; j++){
                        if(i >= 0 && j >= 0 && i < mm.getWidth() && j < mm.getHeight() && mm.getCells()[i][j] == MapEntity.WALL)
                            collision |= willHaveCollision(new Wall(i * GameWorld.getInstance().getBlockLength(), j * GameWorld.getInstance().getBlockLength()));
                    }
                }
                if(collision){
                    setDirection(dirXR, dirYR);
                }
            }
        }));
    }

    public BfsAI(double bfsInterval, int skinId, double x, double y){
        super(skinId, x, y);
        this.bfsInterval = bfsInterval;
        setStrategy();
    }
}
