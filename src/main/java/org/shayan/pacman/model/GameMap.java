package org.shayan.pacman.model;

import javafx.util.Pair;
import org.shayan.pacman.game.MapEntity;

import java.io.FileReader;
import java.util.*;

public class GameMap {
    private int width;
    private int height;
    private MapEntity[][] cells;
    private static Random rnd = new Random();

    public Integer[] read(Scanner scanner) {
        List<Integer> ret = new ArrayList<>();
        Arrays.stream(scanner.nextLine().split(",")).forEach(e->ret.add(Integer.parseInt(e)));
        return ret.toArray(Integer[]::new);
    }

    public GameMap(FileReader reader) {
        Scanner scanner = new Scanner(reader);
        Integer[] tmp = read(scanner);
        width = tmp[0];
        height = tmp[1];
        cells = new MapEntity[width][height];
        for (int i = 0; i < width; i++) {
            tmp = read(scanner);
            for (int j = 0; j < height; j++) {
                cells[i][j] = MapEntity.getTypeById(tmp[j]);
            }
        }
    }

    // this is random generator constructor!
    public GameMap(int n, int m){
        width = n;
        height = m;
        cells = new MapEntity[width][height];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                int r = rnd.nextInt(3);
                if(r == 0)
                    cells[i][j] = MapEntity.EMPTY;
                if(r == 1)
                    cells[i][j] = MapEntity.COIN;
                if(r == 2)
                    cells[i][j] = MapEntity.WALL;
            }
        }
        cells[0][0] = MapEntity.WALL;
        cells[n-1][0] = MapEntity.WALL;
        cells[0][m-1] = MapEntity.WALL;
        cells[n-1][m-1] = MapEntity.WALL;

        for(int i = 0; i < n; i++){
            if(cells[i][0].equals(MapEntity.WALL) || cells[i][m-1].equals(MapEntity.WALL)){
                cells[i][0] = MapEntity.WALL;
                cells[i][m-1] = MapEntity.WALL;
            }
        }
        for(int i = 0; i < m; i++){
            if(cells[0][i].equals(MapEntity.WALL) || cells[n-1][i].equals(MapEntity.WALL)){
                cells[0][i] = MapEntity.WALL;
                cells[n-1][i] = MapEntity.WALL;
            }
        }

        List<Pair<Integer, Integer>> list = new ArrayList<>();
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(cells[i][j].equals(MapEntity.EMPTY))
                    list.add(new Pair<>(i, j));
            }
        }

        Pair<Integer, Integer> pacman = list.get(rnd.nextInt(list.size()));
        list.remove(pacman);

        Pair<Integer, Integer> ai1 = list.get(rnd.nextInt(list.size()));
        list.remove(ai1);

        Pair<Integer, Integer> ai2 = list.get(rnd.nextInt(list.size()));
        list.remove(ai2);

        Pair<Integer, Integer> ai3 = list.get(rnd.nextInt(list.size()));
        list.remove(ai3);

        Pair<Integer, Integer> ai4 = list.get(rnd.nextInt(list.size()));
        list.remove(ai4);

        cells[pacman.getKey()][pacman.getValue()] = MapEntity.PACMAN;
        cells[ai1.getKey()][ai1.getValue()] = MapEntity.AI;
        cells[ai2.getKey()][ai2.getValue()] = MapEntity.AI;
        cells[ai3.getKey()][ai3.getValue()] = MapEntity.AI;
        cells[ai4.getKey()][ai4.getValue()] = MapEntity.AI;
    }

    // this is another random generator
    public GameMap(){
        this(rnd.nextInt(16) + 20, rnd.nextInt(16) + 20);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public MapEntity[][] getCells() {
        return cells;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(width).append(",").append(height).append("\n");
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(j != height-1)
                    sb.append(cells[i][j].id).append(",");
                else
                    sb.append(cells[i][j].id).append("\n");
            }
        }
        return sb.toString();
    }
}
